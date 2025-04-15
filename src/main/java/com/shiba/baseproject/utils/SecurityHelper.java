package com.shiba.baseproject.utils;

import com.shiba.baseproject.security.MyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service

public class SecurityHelper {
    public static String getCurrentUserLogin() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(authentication == null) {
            return null;
        }
        if(authentication.getPrincipal() instanceof MyUserDetails myUserDetails) {
            return myUserDetails.getUsername();
        }
        return null;
    }
}
