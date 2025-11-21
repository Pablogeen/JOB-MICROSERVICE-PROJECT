package com.rey.job.Controller;

import com.rey.job.DTO.JobCompanyReviewDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rey.job.Entity.Job;
import com.rey.job.ServiceImpl.JobServiceImpl;
import com.rey.job.ServiceInterface.JobService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobServiceImpl service;
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobCompanyReviewDTO>> getAllJobs() {
        log.info("About to make a call for Job, Company and Review");
        List<JobCompanyReviewDTO> response = jobService.getAllJobsWithCompanyAndReview();
        log.info("Response received: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postJob(@RequestBody Job job) {
        return new ResponseEntity<>(service.createJob(job), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCompanyReviewDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
     return new ResponseEntity<>(service.deleteJobById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
        public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job job){
          return new ResponseEntity<>(service.updateJob(id, job), HttpStatus.NOT_FOUND);
        }
}
