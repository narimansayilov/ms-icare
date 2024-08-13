package com.icare.controller;

import com.icare.dto.request.UserLoginRequest;
import com.icare.dto.request.UserRegisterRequest;
import com.icare.dto.request.UserUpdateRequest;
import com.icare.dto.response.JwtResponse;
import com.icare.dto.response.UserResponse;
import com.icare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserRegisterRequest request) {
        userService.register(request);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid UserLoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/my")
    public UserResponse getMyProfile(){
        return userService.getMyProfile();
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/update")
    public UserResponse update(@RequestPart("request") @Valid UserUpdateRequest request,
                               @RequestPart("image") MultipartFile image) {
        return userService.update(request, image);
    }
}