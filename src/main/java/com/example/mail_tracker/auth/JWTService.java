package com.example.mail_tracker.auth;

import io.jsonwebtoken.Claims;
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

@Service
public class JWTService {

    private String secretKey = "A63t2/cgCaPufjAbjwRatvH5sd33HQIKohR7gVa4bLM=";

    public JWTService() {
//        this.secretKey = generateStaticSecretKey(); // Generate a static key
        System.out.println("Secret Key: " + secretKey); // Log the key for debugging
    }

    private String generateStaticSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secret key", e);
        }
    }

    public String generateToken(String username, String userId) {
        Map<String, Object> claims = new HashMap<>();
        System.out.println("userId in generateToken " + userId);
        claims.put("userId", userId);

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10000 * 600 * 300))
                .and()
                .signWith(getKey())
                .compact();
    }

    public String getUserId(String token) {
        System.out.println("getUserId " + extractClaim(token, claims -> claims.get("userId", String.class)) );
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim (String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return  claimResolver.apply(claims);
    }

    private  Claims extractAllClaims(String token) {
        return  Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final  String userName = getUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }

}
