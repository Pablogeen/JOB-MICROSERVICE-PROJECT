package com.rey.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyRequestDTO {
    @NotBlank(message = "Name must not be empty") // Cannot be null or empty
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Description must not be empty")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

}
