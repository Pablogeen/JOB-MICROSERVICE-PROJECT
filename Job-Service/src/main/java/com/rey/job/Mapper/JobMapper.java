package com.rey.job.Mapper;

import com.rey.job.DTO.JobCompanyReviewDTO;
import com.rey.job.External.ExternalCompany;

import com.rey.job.External.ExternalReview;
import com.rey.job.Entity.Job;

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
