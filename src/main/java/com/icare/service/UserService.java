package com.icare.service;

import com.icare.dao.entity.LevelEntity;
import com.icare.dao.entity.RoleEntity;
import com.icare.dao.entity.UserEntity;
import com.icare.dao.repository.LevelRepository;
import com.icare.dao.repository.RoleRepository;
import com.icare.dao.repository.UserRepository;
import com.icare.mapper.UserMapper;
import com.icare.model.dto.request.UserLoginRequest;
import com.icare.model.dto.request.UserRegisterRequest;
import com.icare.model.dto.request.UserUpdateRequest;
import com.icare.model.dto.response.JwtResponse;
import com.icare.model.dto.response.UserResponse;
import com.icare.model.exception.AlreadyExistsException;
import com.icare.model.exception.NotFoundException;
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


    public void register(UserRegisterRequest request) {
        log.info("ActionLog.register.start for email is {}", request.getEmail());
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            log.error("ActionLog.register.AlreadyExistsException email is {}", request.getEmail());
            throw  new AlreadyExistsException("EMAIL_ALREADY_EXISTS");
        });
        UserEntity userEntity = UserMapper.INSTANCE.registerRequestToEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRoles(List.of(getRole()));
        userEntity.setLevel(getLevel());
        userRepository.save(userEntity);
        log.info("ActionLog.register.end for email is {}", request.getEmail());
    }

    public JwtResponse login(UserLoginRequest request) {
        log.info("ActionLog.login.start for email is {}", request.getEmail());
        userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            log.error("ActionLog.login.NotFoundException email is {}", request.getEmail());
            return new NotFoundException("EMAIL_NOT_FOUND");
        });
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User principal = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(principal);
        log.info("ActionLog.login.end for email is {}", request.getEmail());
        return new JwtResponse(principal.getUsername(), accessToken);
    }

    public UserResponse getUser(Long id) {
        UserEntity entity = userRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getUser.NotFoundException for id = {}", id);
            return new NotFoundException("USER_NOT_FOUND");
        });
        return UserMapper.INSTANCE.entityToResponse(entity);
    }

    @Transactional
    public UserResponse update(UserUpdateRequest request, MultipartFile image) {
        log.info("ActionLog.update.start for email is {}", request.getEmail());
        String email = getCurrentUsername();
        UserEntity entity = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("ActionLog.login.NotFoundException for email is {}", email);
            return new NotFoundException("USER_NOT_FOUND");
        });
        if(!request.getEmail().equals(entity.getEmail())) {
            if(userRepository.existsByEmail(request.getEmail())) {
                log.error("ActionLog.update.EmailAlreadyExistsException email is {}", email);
                throw new AlreadyExistsException("EMAIL_ALREADY_EXISTS");
            }
        }
        entity.setPhotoUrl(amazonS3Service.uploadFile(image));
        UserMapper.INSTANCE.mapRequestToEntity(entity,request);
        userRepository.save(entity);
        log.info("ActionLog.update.end for email is {}", email);
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
                .orElseGet(() -> roleRepository.save(RoleEntity.builder().name("USER").build()));
    }

    private LevelEntity getLevel() {
        return levelRepository.findByName("Bronze")
                .orElseGet(() -> levelRepository.save(LevelEntity.builder().name("Bronze").adLimit(10).price(0.0).build()));
    }
}
