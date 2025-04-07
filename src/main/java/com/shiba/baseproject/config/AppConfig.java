package com.shiba.baseproject.config;

import com.shiba.baseproject.security.CustomizedRequestFilter;
import com.shiba.baseproject.security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity
public class AppConfig {
    private final CustomizedRequestFilter customizedRequestFilter;
    private final MyUserDetailsService myUserDetailsService;

    //khoi tao spring security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //config spring security for api
        http.csrf(AbstractHttpConfigurer::disable) // disable login interface
                .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(customizedRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //config spring web configure for swagger
    @Bean
    public WebSecurityCustomizer ignoreResources() {
        return webSecurity -> webSecurity
                .ignoring()
                .requestMatchers("/actuator/**", "/v3/api-docs/**",
                        "/swagger-ui/**", "/swagger-ui.html",
                        "webjars/**");
    }



    //bean for password encoder
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService.loadUserDetailsService());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
