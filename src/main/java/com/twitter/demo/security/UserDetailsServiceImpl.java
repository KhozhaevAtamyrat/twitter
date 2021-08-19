package com.twitter.demo.security;

import com.twitter.demo.domain.Admin;
import com.twitter.demo.domain.User;
import com.twitter.demo.repos.AdminRepo;
import com.twitter.demo.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;
    private final AdminRepo adminRepo;

    public UserDetailsServiceImpl(UserRepo userRepo, AdminRepo adminRepo) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(login).orElse(null);
        if (user != null) {
            return SecurityUser.fromCustomUserToUser(user);
        }

        Admin admin = adminRepo.findByUsername(login).orElse(null);
        if (admin != null) {
            return SecurityUser.fromAdminToUser(admin);
        }

        throw new UsernameNotFoundException(String.format("User with login %s does not exists", login));
    }
}
