package com.shiba.baseproject.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String username;
    private String password;
    // optional
    private String platform; //access from web or mobile can use enum Platform {WEB, MOBILE}
    private String deviceToken;
    private String versionApp;
}
