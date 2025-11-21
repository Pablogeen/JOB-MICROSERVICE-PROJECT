package com.rey.job.DTO;


import com.rey.job.External.ExternalCompany;
import com.rey.job.External.ExternalReview;
import lombok.Data;

import java.util.List;

@Data
public class JobCompanyReviewDTO {

    private Long id;
    private String title;
    private String description;
    private String minSalary;
    private String maxSalary;
    private String location;
    private ExternalCompany company;
    private List<ExternalReview> review;

}