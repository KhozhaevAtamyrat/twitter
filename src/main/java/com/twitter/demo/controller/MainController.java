package com.twitter.demo.controller;

import com.twitter.demo.domain.Post;
import com.twitter.demo.domain.User;
import com.twitter.demo.repos.PostRepo;
import com.twitter.demo.repos.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
public class MainController {

    private final PostRepo postRepo;
    private final UserRepo userRepo;

    public MainController(PostRepo postRepo, UserRepo userRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/")
    public String greeting() {
        return "main/greeting";
    }


    @GetMapping("/main")
    public String getPosts(Model model) {

        model.addAttribute("posts", postRepo.findAll());
        model.addAttribute("post", new Post());

        return "main/posts";
    }

    @PostMapping("/main")
    public String addPost(Principal principal,
                          @RequestParam String text,
                          @RequestParam String tag,
                          Model model) {

//        Collection<SimpleGrantedAuthority> authorities =
//                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//
//        if(authorities.containsAll(Role.USER.getAuthorities())){
//            System.out.println("user");
//        }
//        else if(authorities.containsAll(Role.ADMIN.getAuthorities())) {
//            System.out.println("admin");
//        }
        User user = userRepo.findByUsername(principal.getName()).orElse(null);
        postRepo.save(new Post(text, tag, user));

        model.addAttribute("posts", postRepo.findAll());

        return "main/posts";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter,
                         Model model) {

        if (filter == null || filter.isEmpty()) {
            model.addAttribute("posts", postRepo.findAll());
        } else {
            model.addAttribute("posts", postRepo.findByTag(filter));
        }

        return "main/posts";
    }

    @PostMapping("like/{id}")
    public String like(@PathVariable Long id, Principal principal) {
        Post post = postRepo.findById(id).orElse(null);
        User user = userRepo.findByUsername(principal.getName()).orElse(null);

        if (post != null && user != null) {
            post.like(user);
        }

        return "redirect:/main";
    }

}
