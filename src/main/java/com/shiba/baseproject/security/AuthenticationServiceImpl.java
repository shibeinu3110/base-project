package com.shiba.baseproject.security;

import com.shiba.baseproject.common.exception.ErrorMessages;
import com.shiba.baseproject.common.exception.StandardException;
import com.shiba.baseproject.dto.request.SignUpRequest;
import com.shiba.baseproject.dto.response.TokenResponse;
import com.shiba.baseproject.model.Role;
import com.shiba.baseproject.model.User;
import com.shiba.baseproject.model.UserHasRole;
import com.shiba.baseproject.repository.RoleRepository;
import com.shiba.baseproject.repository.UserHasRoleRepository;
import com.shiba.baseproject.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationServiceImpl implements AuthenticationService{
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    UserHasRoleRepository userHasRoleRepository;
    RoleRepository roleRepository;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
        Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        UserHasRole userHasRole = new UserHasRole();
        userHasRole.setUser(user);
        userHasRole.setRole(userRole);
        userHasRoleRepository.save(userHasRole);
        log.info("User {} signed up successfully", signUpRequest.getUsername());
    }

    @Override
    public TokenResponse signIn(SignUpRequest signInRequest) {
        log.info("Getting access token for user: {}", signInRequest.getUsername());
        List<String> authorities = new ArrayList<>();
        try {
            log.info("Authenticating user: {}", signInRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
            log.info("Authorities: {}", authentication.getAuthorities());
            authorities.add(authentication.getAuthorities().toString());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            log.info("Authentication failed for user: {}", signInRequest.getUsername());
            log.info("Exception: {}", e.getMessage());
            throw new StandardException(ErrorMessages.UNAUTHENTICATED, "Invalid username or password");
        }

        // var user = userRepository.findByUsername(signInRequest.getUsername());


        String accessToken = jwtService.generateAccessToken(signInRequest.getUsername(), authorities);
        String refreshToken = jwtService.generateRefreshToken(signInRequest.getUsername(), authorities);

        // tokenService.save(Token.builder().username(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());
        // redisTokenService.saveToken(RedisToken.builder().id(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
