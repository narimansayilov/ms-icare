package com.icare.controller;

import com.icare.model.dto.request.UserLoginRequest;
import com.icare.model.dto.request.UserRegisterRequest;
import com.icare.model.dto.request.UserUpdateRequest;
import com.icare.model.dto.response.JwtResponse;
import com.icare.model.dto.response.UserResponse;
import com.icare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid UserRegisterRequest request) {
        userService.register(request);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid UserLoginRequest request) {
        return userService.login(request);
    }

    @PutMapping("/update/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        return userService.update(id, request);
    }
}
