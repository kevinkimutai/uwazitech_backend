package io.ctrla.claims.config;

import io.ctrla.claims.entity.User;

import io.ctrla.claims.entity.UserPrincipal;
import io.ctrla.claims.repo.PolicyHolderRepository;
import io.ctrla.claims.repo.UserRepository;
import io.ctrla.claims.services.JWTService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;

@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JWTService jwtService;
//    private final UserRepository userRepository;
//
//    public JwtFilter(JWTService jwtService, UserRepository userRepository) {
//        this.jwtService = jwtService;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // Check if the request is for signup or login and bypass the JWT check
//        String requestURI = request.getRequestURI();
//        if (requestURI.startsWith("/api/v1/auth/signup") || requestURI.startsWith("/api/v1/auth/login")||requestURI.startsWith("/api/v1/insurance")||requestURI.startsWith("/api/v1/hospitals")) {
//            filterChain.doFilter(request, response); // Proceed without JWT validation
//            return;
//        }
//
//        String authorizationHeader = request.getHeader("Authorization");
//        String token = null;
//        String email = null;
//        Boolean isVerified = null;
//        Long userId = null;
//        String role = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            token = authorizationHeader.substring(7);
//
//            try {
//                email = jwtService.extractUserName(token);
//                isVerified = jwtService.extractClaim(token, claims -> claims.get("isVerified", Boolean.class));
//                userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));
//                role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
//
//            } catch (Exception e) {
//                if(e instanceof ExpiredJwtException){
//                    response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
//                    response.getWriter().println(e.getMessage());
//                    return;
//                }
//
//                // Log exception and set response status to unauthorized
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//
//            // Make sure isVerified is true and email is not null
//            if (Boolean.TRUE.equals(isVerified) && email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                User user = userRepository.findByEmail(email);
//                if (user == null) {
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    return;
//                }else{
//                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), List.of(new SimpleGrantedAuthority(String.valueOf(user.getRole()))));
//                    if (jwtService.validateToken(token, userDetails)) {
//                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//
////                        // Set hospitalId in SecurityContext for later access
////                        request.setAttribute("userId", userId);
////                        request.setAttribute("role", role);
//
//
//                    } else {
//                        // Log or handle invalid token or user not found
//                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                        return;
//                    }
//                }
//            }
//        }
//
//        // Proceed with the filter chain
//        filterChain.doFilter(request, response);
//    }
//}


public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final PolicyHolderRepository policyHolderRepository;

    public JwtFilter(JWTService jwtService, UserRepository userRepository,PolicyHolderRepository policyHolderRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.policyHolderRepository = policyHolderRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/v1/auth/signup") || requestURI.startsWith("/api/v1/auth/login")
                || requestURI.startsWith("/api/v1/insurance") || requestURI.startsWith("/api/v1/hospitals")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                String email = jwtService.extractUserName(token);
                Boolean isVerified = jwtService.extractClaim(token, claims -> claims.get("isVerified", Boolean.class));
                Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));
                String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
             //   String policyNumber = jwtService.extractClaim(token, claims -> claims.get("policynumber", String.class));


                if (Boolean.TRUE.equals(isVerified) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user;
                    if (email != null && !email.trim().isEmpty()) {
                        user = userRepository.findByEmail(email);
                        System.out.println("EMAIL :" +email);
                    }
                    else{
                        user = userRepository.findById(userId).orElse(null);
                        System.out.println("HAPA 2:"+user);
                    }

                    if (user != null) {

                        UserPrincipal userPrincipal = new UserPrincipal(user, userId, role);

                        if (jwtService.validateToken(token, userPrincipal)) {
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userPrincipal, null, userPrincipal.getAuthorities());
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().println(e.getMessage());
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
