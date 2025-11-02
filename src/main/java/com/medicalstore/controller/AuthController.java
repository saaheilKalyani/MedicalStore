package com.medicalstore.controller;

import com.medicalstore.model.User;
import com.medicalstore.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ✅ Login page (GET)
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    // ✅ Manual login (POST)
    @PostMapping("/login")
    public String manualLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {

        Optional<User> optionalUser = authService.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                session.setAttribute("loggedInUser", user);
                return "redirect:/profile";
            }
        }

        model.addAttribute("error", "Invalid username or password");
        return "auth/login";
    }

    // ✅ Logout (manual)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // ✅ Registration form
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // ✅ Handle registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("rawPassword") String rawPassword,
                               Model model) {
        boolean success = authService.registerUser(user, rawPassword);
        if (!success) {
            model.addAttribute("error", "Username already exists!");
            return "auth/register";
        }
        return "redirect:/login?registered";
    }

    // ✅ Profile (session-based)
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "auth/profile";
    }
    @GetMapping("/orders/create")
    public String createOrderPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "orders/create";
    }
    @GetMapping("/orders")
    public String ordersPage() {
        return "orders/list";
    }

    // ✅ Admin - List Users
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        model.addAttribute("users", authService.findAll());
        return "admin/users";
    }

    // ✅ Admin - Edit User Form
    @GetMapping("/admin/users/edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        authService.findById(id).ifPresent(u -> model.addAttribute("user", u));
        return "admin/editUser";
    }

    // ✅ Admin - Submit Edit
    @PostMapping("/admin/users/edit")
    public String editUserSubmit(@ModelAttribute User user,
                                 @RequestParam(value = "rawPassword", required = false) String rawPassword) {
        if (rawPassword != null && !rawPassword.isBlank()) user.setPassword(rawPassword);
        authService.updateUser(user);
        return "redirect:/admin/users";
    }

    // ✅ Admin - Delete User
    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        authService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
