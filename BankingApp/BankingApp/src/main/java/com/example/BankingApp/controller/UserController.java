package com.example.BankingApp.controller;

import com.example.BankingApp.model.User;
import com.example.BankingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("selectedRole") String role,
            Model model) {

        String validationError = validateSignUp(username, password, confirmPassword);
        if (validationError != null) {
            model.addAttribute("error", validationError);
            return "signup"; // Return to the signup page with an error message
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        boolean success = userService.createUser(user, role);
        if (success) {
            return "redirect:/login"; // Redirect to login page after successful signup
        } else {
            model.addAttribute("error", "User registration failed. Please try again.");
            return "signup";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Return the login page view
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {

        // Here, login logic is handled by Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home"; // Redirect to home page if already authenticated
        }

        // If authentication fails, show an error message
        model.addAttribute("error", "Invalid username or password.");
        return "login"; // Return to the login page with an error message
    }

    private String validateSignUp(String username, String password, String confirmPassword) {
        if (username == null || username.trim().isEmpty()) {
            return "Username is required.";
        }
        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        return null;
    }
}
