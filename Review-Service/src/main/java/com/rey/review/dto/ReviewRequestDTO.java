package com.rey.review.dto;

import lombok.Data;

@Data
public class ReviewRequestDTO {

    private String title;
    private String description;
    private Double rating;
    private Long companyId;
}
