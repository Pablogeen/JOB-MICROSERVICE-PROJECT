package com.rey.job.ServiceImpl;

import lombok.RequiredArgsConstructor;
import com.rey.job.Client.CompanyClient;
import com.rey.job.Client.ReviewClient;
import com.rey.job.DTO.JobCompanyReviewDTO;
import com.rey.job.Entity.Job;
import com.rey.job.External.ExternalCompany;
import com.rey.job.External.ExternalReview;
import com.rey.job.Mapper.JobMapper;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.rey.job.Repository.JobRepository;
import com.rey.job.ServiceInterface.JobService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository repo;
    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;

    @Override
    public List<JobCompanyReviewDTO> getAllJobsWithCompanyAndReview() {
        List<Job> jobs = repo.findAll();
        List<JobCompanyReviewDTO> dto = new ArrayList<>();
        for (Job job: jobs){

            ExternalCompany company =
                    companyClient.getCompany(job.getCompanyId());
            log.info("Job with Company id: {} fetched", job.getCompanyId());

            List<ExternalReview> reviews =
                    reviewClient.getReviews(job.getCompanyId());
            log.info("Reviews found with id: {}", job.getCompanyId());

            JobCompanyReviewDTO jobDTO =
                    JobMapper.mapToJobWithCompanyDto(job, company, reviews);

            dto.add(jobDTO);
        }
        return dto;
    }

    int attempt;

      //@CircuitBreaker(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
  //@Retry(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
  //  @RateLimiter(name="companyBreaker")


//FallBack
//    public List<String> companyBreakerFallback(Exception e){
//        List<String> list = new ArrayList<>();
//        list.add("Ooops!! Something went wrong...");
//        return list;
//    }




@RateLimiter(name = "companyBreaker")
    public String createJob(Job job){
        repo.save(job);
        log.info("Job created successfully");
        return "JOB ADDED SUCCESSFULLY";
    }

    public JobCompanyReviewDTO findById(Long id) {
        Job job = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("JOB NOT  FOUND"));

        ExternalCompany company
                = companyClient.getCompany(job.getCompanyId());
     List<ExternalReview> reviews =
             reviewClient.getReviews(job.getCompanyId());

     JobCompanyReviewDTO jobDTO = new JobCompanyReviewDTO();
     jobDTO.setId(job.getId());
     jobDTO.setTitle(job.getTitle());
     jobDTO.setDescription(job.getDescription());
     jobDTO.setLocation(job.getLocation());
     jobDTO.setMaxSalary(job.getMaxSalary());
     jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setCompany(company);
     jobDTO.setReview(reviews);


//        JobDTO jobDTO =
//                JobMapper.mapToJobWithCompanyDto(job, company, reviews);

        return jobDTO;
    }

        public String deleteJobById(Long id){
         Job job = repo.findById(id)
                 .orElseThrow(()-> new IllegalStateException("JOB NOT FOUND"));
          repo.delete(job);
          return "JOB DELETED SUCCESSFULLY";
        }

    public String updateJob(Long id, Job job) {
        Job existingJob = repo.findById(id)
                .orElseThrow(()-> new IllegalStateException("JOB NOT FOUND"));

        existingJob.setTitle(job.getTitle());
        existingJob.setDescription(job.getDescription());
        existingJob.setMaxSalary(job.getMaxSalary());
        existingJob.setMinSalary(job.getMinSalary());
        existingJob.setLocation(job.getLocation());
         repo.save(existingJob);
         return "JOB UPDATED SUCCESSFULLY";

    }


}

