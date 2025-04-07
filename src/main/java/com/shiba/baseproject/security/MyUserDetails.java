package com.shiba.baseproject.security;

import com.shiba.baseproject.model.Role;
import com.shiba.baseproject.model.User;
import com.shiba.baseproject.model.UserHasRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j(topic = "USER-DETAILS")
public class MyUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = user.getRoles().stream().map(UserHasRole::getRole).toList();
        List<String> roleNames = roles.stream().map(Role::getName).toList();
        if(user.getRoles() == null) {
//            return List.of(new SimpleGrantedAuthority("USER"));
            roleNames.add(new SimpleGrantedAuthority("USER").getAuthority());
        }
        log.info("Roles: {}", roleNames);
        return roleNames.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
