package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import com.example.demo.dto.PasswordUpdateRequest;
import com.example.demo.dto.UserProfile;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest request,HttpServletRequest req) {
        try {
            userService.updatePassword(req,request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateRequest updatedUser,HttpServletRequest request) {
        userService.updateUser(request,updatedUser);
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserProfile> getUserProfile(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getUserDetails(request));
    }

    @PutMapping("/deactivate")
    public ResponseEntity<String> deactivateUser(HttpServletRequest request) {
        userService.deactivateMyAccount(request);
        return ResponseEntity.ok("User deactivated successfully");
    }

}
