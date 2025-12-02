package com.rey.job.ServiceInterface;

import com.rey.job.DTO.JobCompanyReviewDTO;
import com.rey.job.DTO.JobDTO;
import com.rey.job.Entity.Job;

import java.util.List;

public interface JobService {

    List<JobCompanyReviewDTO> getAllJobsWithCompanyAndReview();

    String createJob(JobDTO jobDTO);

    JobCompanyReviewDTO findById(Long id);

    String deleteJobById(Long id);

    String updateJob(Long id, JobDTO jobDto);

    String deleteJobsByCompanyId(Long companyId);
}
