package io.ctrla.claims.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailService, JwtFilter jwtFilter) {
        this.userDetailService = userDetailService;
        this.jwtFilter = jwtFilter;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(c -> c.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests(request ->
//                        request
//                                .requestMatchers("/api/v1/auth/signup",
//                                        "/api/v1/auth/login",
//                                        "api/v1/hospitals",
//                                        "api/v1/insurance",
//                                        "/error")
//                                .permitAll()
//                                .requestMatchers("/api/v1/hospitals/**")
//                                .hasAnyRole("HOSPITAL_ADMIN", "ADMIN")
//                                .requestMatchers("/api/v1/insurance/**")
//                                .hasAnyRole("INSURANCE_ADMIN", "ADMIN")
//                                .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request ->
                        request
                                // Publicly accessible endpoints
                                .requestMatchers("/api/v1/auth/signup",
                                        "/api/v1/auth/login",
                                        "/api/v1/hospitals", // Public access to the base path (e.g., listing)
                                        "/api/v1/insurance",
                                        "/error")
                                .permitAll()
                                // Restricted endpoints with specific roles
                                .requestMatchers("/api/v1/hospitaladmin/**").hasAnyRole("HOSPITAL_ADMIN")  // Restrict hospital admin actions
                                .requestMatchers("/api/v1/insuranceadmin/**").hasAnyRole("INSURANCE_ADMIN")
                                .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN") // Restrict insurance admin actions
                                .requestMatchers("/api/v1/policyholder/**").hasAnyRole("POLICYHOLDER")
                                .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}