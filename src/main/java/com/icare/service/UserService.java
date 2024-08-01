package com.icare.service;

import com.icare.dao.entity.LevelEntity;
import com.icare.dao.repository.LevelRepository;
import lombok.extern.slf4j.Slf4j;
import com.icare.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import com.icare.dao.entity.RoleEntity;
import com.icare.dao.entity.UserEntity;
import org.springframework.stereotype.Service;
import com.icare.dao.repository.RoleRepository;
import com.icare.dao.repository.UserRepository;
import com.icare.model.dto.response.JwtResponse;
import com.icare.model.dto.response.UserResponse;
import com.icare.model.exception.NotFoundException;
import com.icare.model.dto.request.UserLoginRequest;
import com.icare.model.dto.request.UserUpdateRequest;
import com.icare.model.dto.request.UserRegisterRequest;
import org.springframework.security.core.Authentication;
import com.icare.model.exception.AlreadyExistsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
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

    public UserResponse update(Long id, UserUpdateRequest request) {
        log.info("ActionLog.update.start for id is {}", id);
        UserEntity entity = userRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.login.NotFoundException id = {}", id);
            return new NotFoundException("USER_NOT_FOUND");
        });
        UserMapper.INSTANCE.mapRequestToEntity(entity,request);
        userRepository.save(entity);
        log.info("ActionLog.update.end for id is {}", id);
            return UserMapper.INSTANCE.entityToResponse(entity);
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
