package com.shiba.baseproject.controller;

import com.shiba.baseproject.common.StandardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth1")
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
public class TestController {
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/user-test")
    public StandardResponse<String> test1() {
        return StandardResponse.build("Only user can access this method");
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/manager-test")
    public StandardResponse<String> test2() {
        return StandardResponse.build("Only manager can access this method");
    }
}
