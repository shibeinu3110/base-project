package com.shiba.baseproject.dto.request;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SignUpRequest {
    String username;
    String password;
    // optional
    String platform; //access from web or mobile can use enum Platform {WEB, MOBILE}
    String deviceToken;
    String versionApp;
}
