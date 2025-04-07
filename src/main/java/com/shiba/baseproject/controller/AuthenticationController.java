package com.shiba.baseproject.controller;

import com.shiba.baseproject.common.StandardResponse;
import com.shiba.baseproject.dto.request.SignUpRequest;
import com.shiba.baseproject.dto.response.TokenResponse;
import com.shiba.baseproject.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
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
        return StandardResponse.build(tokenResponse);
    }


}
