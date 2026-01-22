package com.rey.userService.controller;

import com.rey.userService.dto.LoginRequestDto;
import com.rey.userService.dto.UserRequestDto;
import com.rey.userService.dto.UserResponseDto;
import com.rey.userService.service.ServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequest){
        log.info("Request made to login");
        String loginResponse = serviceInterface.loginUser(loginRequest);
        log.info("User logged in successfully");
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }


}
