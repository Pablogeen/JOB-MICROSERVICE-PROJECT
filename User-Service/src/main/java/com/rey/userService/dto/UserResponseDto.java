package com.rey.userService.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserResponseDto implements Serializable {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

}
