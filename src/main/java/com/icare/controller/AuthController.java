package com.icare.controller;

import com.icare.dto.request.ResetPassword;
import com.icare.dto.request.UserLoginRequest;
import com.icare.dto.request.UserRegisterRequest;
import com.icare.dto.request.VerificationRequest;
import com.icare.dto.response.JwtResponse;
import com.icare.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserRegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid UserLoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/verify")
    public void verifyAccount(@RequestBody @Valid VerificationRequest request){
        authService.verifyAccount(request);
    }

    @PostMapping("/resend-verification-code")
    public void resendVerificationCode(@RequestParam("email") String email){
        authService.resendVerificationCode(email);
    }

    @PostMapping("/update-password")
    public void updatePassword(@RequestParam("email") String email){
        authService.updatePassword(email);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam("token") String token,
                              @RequestBody ResetPassword resetPassword){
        authService.resetPassword(token, resetPassword);
    }
}
