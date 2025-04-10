package com.shiba.baseproject.security;

import com.shiba.baseproject.model.User;
import com.shiba.baseproject.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-DETAILS-SERVICE")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MyUserDetailsServiceImpl implements MyUserDetailsService  {
    UserRepository userRepository;
    @Override
    public UserDetailsService loadUserDetailsService() {

        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return new MyUserDetails(user);
        };
    }
}
