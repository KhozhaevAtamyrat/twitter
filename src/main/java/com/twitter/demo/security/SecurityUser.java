package com.twitter.demo.security;

import com.twitter.demo.domain.Admin;
import com.twitter.demo.domain.User;
import com.twitter.demo.domain.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
public class SecurityUser implements UserDetails {

    private final String login;
    private final String password;
    private final Set<SimpleGrantedAuthority> authorities;

    public SecurityUser(String login, String password, Set<SimpleGrantedAuthority> authorities) {
        this.login = login;
        this.password = password;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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

    public static UserDetails fromCustomUserToUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                true, true, true, true,
                Role.USER.getAuthorities()
        );
    }

    public static UserDetails fromAdminToUser(Admin admin) {
        return new org.springframework.security.core.userdetails.User(
                admin.getUsername(), admin.getPassword(),
                true, true, true, true,
                Role.ADMIN.getAuthorities()
        );
    }

}
