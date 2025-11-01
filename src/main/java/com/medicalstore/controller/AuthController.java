package com.medicalstore.controller;

import com.medicalstore.model.User;
import com.medicalstore.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Invalid username or password");
        if (logout != null) model.addAttribute("message", "You have been logged out");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("rawPassword") String rawPassword,
                               Model model) {
        boolean ok = authService.registerUser(user, rawPassword);
        if (!ok) {
            model.addAttribute("error", "Username already exists");
            return "auth/register";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        authService.findByUsername(principal.getName()).ifPresent(u -> model.addAttribute("user", u));
        return "auth/profile";
    }

    // Admin - Manage Users
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        model.addAttribute("users", authService.findAll());
        return "admin/users";
    }

    @GetMapping("/admin/users/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/addUser";
    }

    @PostMapping("/admin/users/add")
    public String addUserSubmit(@ModelAttribute User user, @RequestParam("rawPassword") String rawPassword) {
        authService.registerUser(user, rawPassword);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        authService.findById(id).ifPresent(u -> model.addAttribute("user", u));
        return "admin/editUser";
    }

    @PostMapping("/admin/users/edit")
    public String editUserSubmit(@ModelAttribute User user, @RequestParam(value = "rawPassword", required = false) String rawPassword) {
        if (rawPassword != null && !rawPassword.isBlank()) user.setPassword(rawPassword);
        else user.setPassword("");
        authService.updateUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // Test endpoints
    @GetMapping("/auth/ping")
    @ResponseBody
    public String ping() {
        return "auth-module is up: " + LocalDateTime.now();
    }

    @GetMapping("/auth/test-db")
    @ResponseBody
    public String testDb() {
        try {
            int count = authService.userCount();
            return "DB OK, users count = " + count;
        } catch (Exception e) {
            return "DB ERROR: " + e.getMessage();
        }
    }
}