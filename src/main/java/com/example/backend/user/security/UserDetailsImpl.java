package com.example.backend.user.security;

import com.example.backend.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final String email;
    private final List<String> roles;
    private String password;

    private String nick;

    public UserDetailsImpl(String email, List<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public UserDetailsImpl(String email, List<String> roles, String password, String nick) {
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.nick = nick;
    }

    public UserDetailsImpl(User user){
        this.email = user.getEmail();
        this.roles = user.getRole();
    }

    public String getNick() {
        return nick;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}