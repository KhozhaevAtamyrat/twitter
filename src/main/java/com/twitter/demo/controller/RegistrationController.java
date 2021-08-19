package com.twitter.demo.controller;

import com.twitter.demo.domain.User;
import com.twitter.demo.repos.AdminRepo;
import com.twitter.demo.repos.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final UserRepo userRepo;
    private final AdminRepo adminRepo;

    public RegistrationController(UserRepo userRepo, AdminRepo adminRepo) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
    }

    @GetMapping("/registration")
    public String registration() {
        return "/main/registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (userRepo.findByUsername(user.getUsername()).isPresent() ||
                adminRepo.findByUsername(user.getUsername()).isPresent()) {

            model.addAttribute("user", user);
            model.addAttribute("message", "Пользователь с таким логином уже существует!");
            return "main/registration";
        }
        user.setPassword(getPasswordEncoder().encode(user.getPassword()));

        userRepo.save(user);

        return "redirect:/login";
    }

    private PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
