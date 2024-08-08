package com.icare.controller;

import com.icare.model.dto.request.UserLoginRequest;
import com.icare.model.dto.request.UserRegisterRequest;
import com.icare.model.dto.request.UserUpdateRequest;
import com.icare.model.dto.response.JwtResponse;
import com.icare.model.dto.response.UserResponse;
import com.icare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    public UserResponse getUserDetails(Authentication authentication) {
        return userService.getUser(authentication.getName());
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid UserLoginRequest request) {
        return userService.login(request);
    }

    @PutMapping("/update")
    public UserResponse update(Authentication authentication,
                               @RequestPart("request") @Valid UserUpdateRequest request,
                               @RequestPart("image") MultipartFile image) {
        return userService.update(request, image, authentication.getName());
    }
}