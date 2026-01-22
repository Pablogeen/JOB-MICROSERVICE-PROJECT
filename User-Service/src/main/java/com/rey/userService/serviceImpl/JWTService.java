package com.rey.userService.serviceImpl;

import com.rey.userService.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JWTService {

    private final UserServiceDetails userServiceDetails;

    SecretKey sk;
    String secretKey;

    {
        try {
            sk = KeyGenerator.getInstance("hmacSHA256").generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            log.error("There is an error in the secret: {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public String generateToken(@NotBlank @NotNull String username) {
        Map<String, Object> claims = new HashMap<>();

        User userDetails =
                (User) userServiceDetails.loadUserByUsername(username.strip());

        List<String> authorities =
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .toList();

        return Jwts.builder()
                .claim("authorities", authorities)
                .addClaims(claims)
                .setSubject(username.strip())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60*60*30))
                .signWith(getKey())
                .compact();
    }

    public SecretKey getKey(){
        byte[] newKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(newKey);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return  claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
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

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
}
