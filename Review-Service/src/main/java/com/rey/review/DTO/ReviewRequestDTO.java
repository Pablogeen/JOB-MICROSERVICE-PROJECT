package com.rey.review.DTO;

import lombok.Data;

@Data
public class ReviewRequestDTO {

    private String title;
    private String description;
    private Double rating;
    private Long companyId;
}
