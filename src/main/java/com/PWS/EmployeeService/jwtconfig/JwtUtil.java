package com.PWS.EmployeeService.jwtconfig;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String secret ;
    private int refreshExpirationDateInMs;
    private int jwtExpirationInMs;


    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }
//this is for expiring token
    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    //this is for refreshing the  expiration token
    @Value("${jwt.refreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }
    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();

    }


//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//

//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername())
//                && !isTokenExpired(token)
//                && !InvalidTokenStore.isTokenInvalid(token)); // Check if token is invalidated
//    }

    public String getUsernameFromToken(String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();
        return claims.getSubject();


    }



    public boolean validateToken(String jwtToken) {
        if (isTokenBlacklisted(jwtToken)) {
            throw new BadCredentialsException("TOKEN_HAS_BEEN_REVOKED");
        }
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }
    }
    private Set<String> blacklistedTokens = new HashSet<>();

    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
    public void invalidateToken(String authToken) {
        addToBlacklist(authToken);
    }






}
