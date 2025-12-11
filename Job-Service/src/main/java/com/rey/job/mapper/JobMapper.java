package com.rey.job.mapper;

import com.rey.job.dto.JobCompanyReviewDTO;
import com.rey.job.external.ExternalCompany;

import com.rey.job.external.ExternalReview;
import com.rey.job.entity.Job;

import java.util.List;

public class JobMapper {

    public static JobCompanyReviewDTO mapToJobWithCompanyDto(
            Job job,
            ExternalCompany company,
                    List<ExternalReview> reviews
    ){
        JobCompanyReviewDTO jobDTO = new JobCompanyReviewDTO();
        jobDTO.setId(job.getId());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setCompany(company);
        jobDTO.setReview(reviews);

        return jobDTO;
    }
}
