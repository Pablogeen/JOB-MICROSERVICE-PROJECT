package com.rey.userService.service;

import com.rey.userService.dto.LoginRequestDto;
import com.rey.userService.dto.UserRequestDto;
import com.rey.userService.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Component
public interface ServiceInterface {

      UserResponseDto register(@Valid UserRequestDto userRequestDto);

     String loginUser(@Valid LoginRequestDto loginRequest);

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long id);

    UserResponseDto revokeAdminRole(Long id);

    UserResponseDto assignAdminRole(Long id);

    void uploadProfile(Long id, MultipartFile file) throws IOException;
}
