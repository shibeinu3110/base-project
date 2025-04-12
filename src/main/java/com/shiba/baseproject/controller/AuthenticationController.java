package com.shiba.baseproject.controller;

import com.shiba.baseproject.common.StandardResponse;
import com.shiba.baseproject.common.enumerate.TokenType;
import com.shiba.baseproject.dto.request.SignUpRequest;
import com.shiba.baseproject.dto.response.TokenResponse;

import com.shiba.baseproject.security.AuthenticationService;
import com.shiba.baseproject.security.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationController {
    AuthenticationService authenticationService;
    JwtService jwtService;

    @PostMapping("/sign-up")
    public StandardResponse<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        log.info("Sign up");
        authenticationService.signUp(signUpRequest);
        return StandardResponse.build("Sign up");
    }

    @PostMapping("/sign-in")
    public StandardResponse<TokenResponse> signIn(@RequestBody SignUpRequest signInRequest) {
        log.info("Sign in");
        TokenResponse tokenResponse = authenticationService.signIn(signInRequest);
        String roles = jwtService.extractRoles(tokenResponse.getAccessToken(), TokenType.ACCESS_TOKEN);
        return StandardResponse.build(tokenResponse, "Sign in successfully with role " + roles);
    }





}
