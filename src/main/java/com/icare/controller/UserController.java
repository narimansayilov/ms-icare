package com.icare.controller;

import com.icare.dto.criteria.UserCriteriaRequest;
import com.icare.dto.request.UserUpdateRequest;
import com.icare.dto.response.UserResponse;
import com.icare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping("/my")
    public UserResponse getMyProfile(){
        return userService.getMyProfile();
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/all")
    public List<UserResponse> getAllUsers(Pageable pageable, UserCriteriaRequest criteriaRequest) {
        return userService.getAllUsers(pageable, criteriaRequest);
    }

    @PutMapping("/update")
    public UserResponse update(@RequestPart("request") @Valid UserUpdateRequest request,
                               @RequestPart("image") MultipartFile image) {
        return userService.update(request, image);
    }

    @PatchMapping("/{id}/activate")
    public void activateUser(@PathVariable Long id) {
        userService.activateUser(id);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
    }

    @PatchMapping("/set-role")
    public void setRole(@RequestParam(name = "user") Long userId,
                        @RequestParam(name = "role") Long roleId) {
        userService.setRole(userId, roleId);
    }
}