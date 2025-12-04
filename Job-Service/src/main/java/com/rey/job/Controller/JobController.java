package com.rey.job.Controller;

import com.rey.job.DTO.JobCompanyReviewDTO;
import com.rey.job.DTO.JobRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rey.job.ServiceInterface.JobService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobCompanyReviewDTO>> getAllJobs() {
        log.info("About to make a call for Job, Company and Review");
        List<JobCompanyReviewDTO> response = jobService.getAllJobsWithCompanyAndReview();
        log.info("Response received: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postJob(@RequestBody JobRequestDTO jobDTO) {
        log.info("Received request to post Job: {}",jobDTO);
        String jobResponse = jobService.createJob(jobDTO);
        log.info("Return response from job posted");
        return new ResponseEntity<>(jobResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCompanyReviewDTO> findById(@PathVariable("id") Long id) {
        log.info("About to make request with id: {}",id);
        JobCompanyReviewDTO response = jobService.findById(id);
        log.info("Gotten response from id: {}",response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        log.info("Request to delete job with id: {}",id);
        String response = jobService.deleteJobById(id);
        log.info("Job Deleted successfully");
     return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/company/{companyId}")
    public ResponseEntity<String> deleteJobsByCompanyId(@PathVariable("companyId") Long companyId) {
        log.info("Request to delete job with companyId: {}",companyId);
        String response = jobService.deleteJobsByCompanyId(companyId);
        log.info("Jobs with companyId: {} Deleted successfully",companyId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
        public ResponseEntity<String> updateJob(@PathVariable("id") Long id,
                                                @RequestBody JobRequestDTO jobDto){
        log.info("Updating job with id: {}",id);
        String updatedResponse = jobService.updateJob(id, jobDto);
        log.info("Updated Job: {}",jobDto);
          return new ResponseEntity<>(updatedResponse, HttpStatus.NOT_FOUND);
        }
}
