package com.shiba.baseproject.security;

import com.shiba.baseproject.common.enumerate.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {
    String generateAccessToken(String username, List<String> roles);
    String generateRefreshToken(String username, List<String> roles);
    String generateResetToken(String username, List<String> roles);
    String extractUsername(String token, TokenType type);
    boolean isValidToken(String token, TokenType type, UserDetails userDetails);
    String extractRoles(String token, TokenType type);
}
