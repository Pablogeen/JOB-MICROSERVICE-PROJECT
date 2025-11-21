package com.rey.job.ServiceInterface;

import com.rey.job.DTO.JobCompanyReviewDTO;

import java.util.List;

public interface JobService {

    List<JobCompanyReviewDTO> getAllJobsWithCompanyAndReview();
}
