package com.shiba.baseproject.security;

import com.shiba.baseproject.common.enumerate.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService{
    @Override
    public String generateAccessToken( String username, List<String> roles) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", roles);

        return generateToken(claims, username);
    }

    @Override
    public String generateRefreshToken( String username, List<String> roles) {


        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", roles);

        return generateRefreshToken(claims, username);
    }

    @Override
    public String generateResetToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", roles);

        return generateResetToken(claims, username);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        log.info("Extracting username from token: {}", token);
        return extractClaim(token, type, Claims::getSubject);
    }
    @Override
    public String extractRoles(String token, TokenType type) {
        log.info("Extracting roles from token: {}", token);

        return extractClaim(token, type, claims -> claims.get("roles").toString());
    }

    @Override
    public boolean isValidToken(String token, TokenType type, UserDetails userDetails) {
        if(type.equals(TokenType.ACCESS_TOKEN)) {
            final String username = extractUsername(token, TokenType.ACCESS_TOKEN);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, TokenType.ACCESS_TOKEN));
        } else if (type.equals(TokenType.REFRESH_TOKEN)) {
            final String username = extractUsername(token, TokenType.REFRESH_TOKEN);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, TokenType.REFRESH_TOKEN));
        } else if (type.equals(TokenType.RESET_TOKEN)) {
            final String username = extractUsername(token, TokenType.RESET_TOKEN);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, TokenType.RESET_TOKEN));

        } else {
            throw new IllegalArgumentException("Invalid token type: " + type);
        }
    }


    private boolean isTokenExpired(String token, TokenType tokenType) {
        log.info("Checking if token is expired: {}", token);
        final Date expiration = extractClaim(token, tokenType, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private String generateToken(Map<String, Object> claims, String username) {
        log.info("Generating token for user: {}", username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) //expire in 10 minutes
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String username) {
        log.info("Generating token for user: {}", username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) //expire in 7 days
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateResetToken(Map<String, Object> claims, String username) {
        log.info("Generating token for user: {}", username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //expire in 1 hour
                .signWith(getKey(TokenType.RESET_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        log.info("Getting key for token type: {}", type);
        // decode and create secret key by HMAC
        switch (type) {
            case ACCESS_TOKEN:
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode("SbDaGffItwnOwPtWLfPOuKSnszZ0PdLYxkXMA+Mv0to="));
            case REFRESH_TOKEN:
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode("IPLtGytwCjsXuLzHzBL+Yq0mNGsJXDlJuOiFJfz+Kkw="));
            case RESET_TOKEN:
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode("halnER3e8Cr2b0i+4PSfBJh03G51SIc4rX/+b0iyVWU="));
            default:
                throw new IllegalArgumentException("Invalid token type: " + type);
        }
    }

    private Claims extractAllClaims(String token, TokenType type) {
        log.info("Extracting all claims from token: {}", token);

        if(type.equals(TokenType.ACCESS_TOKEN)) {
            try {
                return Jwts.parser().setSigningKey("SbDaGffItwnOwPtWLfPOuKSnszZ0PdLYxkXMA+Mv0to=")
                        .parseClaimsJws(token).getBody();
            } catch (SignatureException | ExpiredJwtException e) {
                log.error("Error extracting claims from token: {}", e.getMessage());
                throw new AccessDeniedException("Invalid token, message=" + e.getMessage());
            }
        } else if (type.equals(TokenType.REFRESH_TOKEN)) {
            try {
                return Jwts.parser().setSigningKey("IPLtGytwCjsXuLzHzBL+Yq0mNGsJXDlJuOiFJfz+Kkw=")
                        .parseClaimsJws(token).getBody();
            } catch (SignatureException | ExpiredJwtException e) {
                log.error("Error extracting claims from token: {}", e.getMessage());
                throw new AccessDeniedException("Invalid token, message=" + e.getMessage());
            }
        } else if (type.equals(TokenType.RESET_TOKEN)) {
            try {
                return Jwts.parser().setSigningKey("halnER3e8Cr2b0i+4PSfBJh03G51SIc4rX/+b0iyVWU=")
                        .parseClaimsJws(token).getBody();
            } catch (SignatureException | ExpiredJwtException e) {
                log.error("Error extracting claims from token: {}", e.getMessage());
                throw new AccessDeniedException("Invalid token, message=" + e.getMessage());
            }
        }
        else {
            throw new IllegalArgumentException("Invalid token type: " + type);
        }
    }
    private <T > T extractClaim(String token, TokenType type, Function<Claims, T> claimExtractor)  {
        log.info("Extracting claim from token: {}", token);
        final Claims claims = extractAllClaims(token, type);
        return claimExtractor.apply(claims);
    }
}
