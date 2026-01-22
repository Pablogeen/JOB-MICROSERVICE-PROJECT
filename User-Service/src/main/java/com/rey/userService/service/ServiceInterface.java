package com.rey.userService.service;

import com.rey.userService.dto.LoginRequestDto;
import com.rey.userService.dto.UserRequestDto;
import com.rey.userService.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public interface ServiceInterface {

      UserResponseDto register(@Valid UserRequestDto userRequestDto);

     String loginUser(@Valid LoginRequestDto loginRequest);
}
