package com.rey.job.serviceInterface;

import com.rey.job.dto.JobCompanyReviewDTO;
import com.rey.job.dto.JobRequestDTO;

import java.util.List;

public interface JobService {

    List<JobCompanyReviewDTO> getAllJobsWithCompanyAndReview();

    String createJob(JobRequestDTO jobDTO);

    JobCompanyReviewDTO findById(Long id);

    String deleteJobById(Long id);

    String updateJob(Long id, JobRequestDTO jobDto);

    String deleteJobsByCompanyId(Long companyId);
}
