package com.icare.service;

import com.icare.dto.request.ResetPassword;
import com.icare.dto.request.UserLoginRequest;
import com.icare.dto.request.UserRegisterRequest;
import com.icare.dto.request.VerificationRequest;
import com.icare.dto.response.JwtResponse;
import com.icare.entity.LevelEntity;
import com.icare.entity.RoleEntity;
import com.icare.entity.UserEntity;
import com.icare.exception.*;
import com.icare.mapper.UserMapper;
import com.icare.repository.LevelRepository;
import com.icare.repository.RoleRepository;
import com.icare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final MailService mailService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final LevelRepository levelRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(UserRegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new AlreadyExistsException("EMAIL_ALREADY_EXISTS");
        });
        UserEntity userEntity = UserMapper.INSTANCE.registerRequestToEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRoles(List.of(getRole()));
        userEntity.setLevel(getLevel());
        userEntity.setVerificationCode(generateVerificationCode());
        userEntity.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        userRepository.save(userEntity);
        String subject = "Verification Code";
        mailService.sendEmail(userEntity.getEmail(), subject, userEntity.getVerificationCode());
    }

    public JwtResponse login(UserLoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        if(!user.getIsVerified()){
            throw new NotVerifiedException("USER_NOT_VERIFIED");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User principal = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(principal);
        return new JwtResponse(principal.getUsername(), accessToken);
    }

    public void verifyAccount(VerificationRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        if(!user.getVerificationCode().equals(request.getVerificationCode())){
            throw new InvalidTokenException("INVALID_VERIFICATION_CODE");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime codeGeneratedAt = user.getExpiryDate();
        if(codeGeneratedAt != null && now.isAfter(codeGeneratedAt)){
            throw new TokenExpiredException("VERIFICATION_CODE_EXPIRED");
        }
        user.setIsVerified(true);
        userRepository.save(user);
    }

    public void resendVerificationCode(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        user.setVerificationCode(generateVerificationCode());
        user.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        String subject = "Verification Code";
        mailService.sendEmail(user.getEmail(), subject, user.getVerificationCode());
    }

    public void updatePassword(String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        String token = jwtService.generatePasswordResetToken(email);
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
        String subject = "Password Reset";
        mailService.sendEmail(user.getEmail(), subject, resetLink);
    }

    public void resetPassword(String token, ResetPassword resetPassword){
        if(!jwtService.validateToken(token)){
            throw new InvalidTokenException("INVALID_TOKEN");
        }
        String email = jwtService.extractEmail(token);
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
        userRepository.save(user);
    }

    private RoleEntity getRole() {
        return roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(RoleEntity.builder()
                        .name("USER")
                        .build()));
    }

    private LevelEntity getLevel() {
        LevelEntity level = levelRepository.findByName("Bronze")
                .orElseGet(() -> levelRepository.save(LevelEntity.builder()
                        .name("Bronze")
                        .adLimit(10)
                        .price(0.0)
                        .userCount(0)
                        .build()));
        level.setUserCount(level.getUserCount() + 1);
        return level;
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(1000, 10000);
        return String.valueOf(otp);
    }
}
