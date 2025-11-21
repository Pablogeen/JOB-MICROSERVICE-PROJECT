package com.rey.review.DTO;

import lombok.Data;

@Data
public class ReviewDTO {

    private String title;
    private String description;
    private Double rating;
    private Long companyId;
}
