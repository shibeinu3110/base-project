package com.shiba.baseproject.security;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface MyUserDetailsService {
    UserDetailsService loadUserDetailsService();
}
