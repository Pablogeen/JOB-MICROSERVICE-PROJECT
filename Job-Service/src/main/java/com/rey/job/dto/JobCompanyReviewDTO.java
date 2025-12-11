package com.rey.job.dto;


import com.rey.job.external.ExternalCompany;
import com.rey.job.external.ExternalReview;
import lombok.Data;

import java.util.List;

@Data
public class JobCompanyReviewDTO {

    private Long id;
    private String title;
    private String description;
    private Long minSalary;
    private Long maxSalary;
    private String location;
    private ExternalCompany company;
    private List<ExternalReview> review;

}