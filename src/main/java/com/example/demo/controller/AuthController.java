package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthResponse;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserService userService;

    public AuthController(OtpService otpService, EmailService emailService, UserService userService) {
        this.otpService = otpService;
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String response = userService.registerUser(user);
        return response.startsWith("User already exists")
                ? ResponseEntity.status(HttpStatus.CONFLICT).body(response)
                : ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        try {
            AuthResponse authResponse = userService.login(user);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.getToken())
                    .body(authResponse);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse(e.getMessage(), null));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestParam String email) {
        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);

        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(email, otp);
        
        if (isValid) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP verified successfully.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired OTP."));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String email, @RequestParam String otp,
            @RequestParam String newPassword) {
        if (!otpService.validateOtp(email, otp)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid OTP. Cannot reset password."));
        }

        boolean isUpdated = userService.resetPassword(email, newPassword);
        if (!isUpdated) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found."));
        }

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }

}
