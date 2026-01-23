package com.rey.userService.controller;

import com.rey.userService.dto.LoginRequestDto;
import com.rey.userService.dto.PageResponse;
import com.rey.userService.dto.UserRequestDto;
import com.rey.userService.dto.UserResponseDto;
import com.rey.userService.service.ServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final ServiceInterface serviceInterface;


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody UserRequestDto userRequestDto){
        log.info("Request made to register user");
        UserResponseDto userResponse = serviceInterface.register(userRequestDto);
        log.info("User has ben registered successfully");
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<?> uploadProfilePicture(@PathVariable Long id,@RequestPart MultipartFile file) throws IOException {
        log.info("Request to upload a profile picture");
        serviceInterface.uploadProfile(id, file);
        log.info("Profile picture uploaded successfully");
        return ResponseEntity.ok("Profile Picture uploaded successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequest){
        log.info("Request made to login");
        String loginResponse = serviceInterface.loginUser(loginRequest);
        log.info("User logged in successfully");
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-users")
    public PageResponse<UserResponseDto> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDto> userPage = serviceInterface.getAllUsers(pageable);
        log.info("Got {} users on page {}", userPage.getNumberOfElements(), page);
        return new PageResponse<>(userPage.getContent(), userPage.getNumber(),
                userPage.getSize(), userPage.getTotalElements(), userPage.getTotalPages());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        log.info("Getting the details of user with id: {}",id);
        UserResponseDto userResponse = serviceInterface.getUserById(id);
        log.info("Got the user: {}",userResponse);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/assign-admin")
    public ResponseEntity<UserResponseDto> assignAdminRole(@PathVariable Long id) {
        log.info("Assigning Admin role to a user");
        UserResponseDto assignRole= serviceInterface.assignAdminRole(id);
        log.info("Admin role assigned to user");
        return new ResponseEntity<>(assignRole, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/revoke-admin")
    public ResponseEntity<UserResponseDto> revokeAdminRole(@PathVariable Long id) {
        log.info("Revoking Admin role to a User");
        UserResponseDto revokedRole= serviceInterface.revokeAdminRole(id);
        log.info("Admin role revoked");
        return new ResponseEntity<>(revokedRole, HttpStatus.OK);
    }

}
