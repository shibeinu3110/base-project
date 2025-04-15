package com.shiba.baseproject.security;

import com.shiba.baseproject.common.enumerate.TokenType;
import com.shiba.baseproject.common.exception.ErrorMessages;
import com.shiba.baseproject.common.exception.StandardException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j(topic = "CUSTOMIZED-REQUEST-FILTER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomizedRequestFilter extends OncePerRequestFilter {
    JwtService jwtService;
    ApplicationContext context;
    MyUserDetailsService myUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("{} request to {}", request.getMethod(), request.getRequestURI());
        String headerAuth = request.getHeader("Authorization");
        String username = null;
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            headerAuth = headerAuth.substring(7);
            try {
                username = jwtService.extractUsername(headerAuth, TokenType.ACCESS_TOKEN);
            } catch (Exception e) {
                log.info("Exception: {}", e.getMessage());

                throw new StandardException(ErrorMessages.COMMON_ERROR, e.getMessage());
            }

            UserDetails userDetails = myUserDetailsService.loadUserDetailsService().loadUserByUsername(username);
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            jwtService.isValidToken(headerAuth, TokenType.ACCESS_TOKEN, userDetails);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(authToken);
            SecurityContextHolder.setContext(securityContext);

        }
        filterChain.doFilter(request, response);
    }
}
