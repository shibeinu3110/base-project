package com.shiba.baseproject.security;

import com.shiba.baseproject.dto.request.SignUpRequest;
import com.shiba.baseproject.dto.response.TokenResponse;

public interface AuthenticationService {
    void signUp(SignUpRequest signUpRequest);
    TokenResponse signIn(SignUpRequest signInRequest);
}
