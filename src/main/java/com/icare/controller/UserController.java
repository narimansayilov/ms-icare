package com.icare.controller;

import com.icare.model.dto.request.UserLoginRequest;
import com.icare.model.dto.request.UserRegisterRequest;
import com.icare.model.dto.request.UserUpdateRequest;
import com.icare.model.dto.response.JwtResponse;
import com.icare.model.dto.response.UserResponse;
import com.icare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid UserRegisterRequest request) {
        userService.register(request);
    }

    @GetMapping("/details")
    public UserResponse getUserDetails() {
        String email = getCurrentUsername();
        return userService.getUser(email);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid UserLoginRequest request) {
        return userService.login(request);
    }

    @PutMapping("/update")
    public UserResponse update(@RequestPart("request") @Valid UserUpdateRequest request,
                               @RequestPart("image") MultipartFile image) {
        String email = getCurrentUsername();
        return userService.update(request, image, email);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}