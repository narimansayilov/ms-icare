package com.icare.service;

import com.icare.entity.LevelEntity;
import com.icare.entity.RoleEntity;
import com.icare.entity.UserEntity;
import com.icare.repository.LevelRepository;
import com.icare.repository.RoleRepository;
import com.icare.repository.UserRepository;
import com.icare.mapper.UserMapper;
import com.icare.dto.request.UserLoginRequest;
import com.icare.dto.request.UserRegisterRequest;
import com.icare.dto.request.UserUpdateRequest;
import com.icare.dto.response.JwtResponse;
import com.icare.dto.response.UserResponse;
import com.icare.exception.AlreadyExistsException;
import com.icare.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final AmazonS3Service amazonS3Service;
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
        userRepository.save(userEntity);
    }

    public JwtResponse login(UserLoginRequest request) {
        userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new NotFoundException("EMAIL_NOT_FOUND"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User principal = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(principal);
        return new JwtResponse(principal.getUsername(), accessToken);
    }

    public UserResponse getUser(Long id) {
        UserEntity entity = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        return UserMapper.INSTANCE.entityToResponse(entity);
    }

    public UserResponse getMyProfile(){
        String email = getCurrentUsername();
        UserEntity entity = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        return UserMapper.INSTANCE.entityToResponse(entity);
    }

    @Transactional
    public UserResponse update(UserUpdateRequest request, MultipartFile image) {
        String email = getCurrentUsername();
        UserEntity entity = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        if(!request.getEmail().equals(entity.getEmail())) {
            if(userRepository.existsByEmail(request.getEmail())) {
                throw new AlreadyExistsException("EMAIL_ALREADY_EXISTS");
            }
        }
        entity.setPhotoUrl(amazonS3Service.uploadFile(image));
        UserMapper.INSTANCE.mapRequestToEntity(entity,request);
        userRepository.save(entity);
        return UserMapper.INSTANCE.entityToResponse(entity);
    }

    public String getCurrentUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
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
}