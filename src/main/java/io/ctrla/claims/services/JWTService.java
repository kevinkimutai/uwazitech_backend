package io.ctrla.claims.services;

import io.ctrla.claims.dto.user.UserDto;
import io.ctrla.claims.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;


@Service
public class JWTService {

    private String secretKey = "";

  public JWTService() {
      try {
          KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
          SecretKey sk = keygen.generateKey();
          secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
      } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
      }
  }


    public String generateToken(User user) {
        Map<String,Object> claims = new HashMap<>();

        // Adding claims to the token
        claims.put("role", user.getRole());
        claims.put("isVerified", user.getIsVerified());
        claims.put("userId", user.getUserId());


        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(String.valueOf(user.getEmail()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60L * 60 * 24 * 30 * 1000))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
     byte[] keyBytes = Decoders.BASE64.decode(secretKey);
     return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUserName(String token) {
        // extract the username from jwt token
        System.out.println("Extract Username from JWT");
        System.out.println(extractClaim(token, Claims::getSubject));
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        System.out.println("Extract all claims");
        System.out.println(claimResolver.apply(claims));
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
