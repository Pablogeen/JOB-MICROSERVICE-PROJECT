package JOB.Impl;

import JOB.Client.CompanyClient;
import JOB.Client.ReviewClient;
import JOB.DTO.JobDTO;
import JOB.External.ExternalCompany;
import JOB.External.ExternalReview;
import JOB.Mapper.JobMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class JobService {
    private JobRepository repo;
    private CompanyClient companyClient;
    private ReviewClient reviewClient;



    int attempt;

      //@CircuitBreaker(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
  //@Retry(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name="companyBreaker")
    public List<JobDTO> getAllJobs() {
      System.out.println("Attempt: "+attempt++);
        List<Job> jobs = repo.findAll();
        List<JobDTO> dto = new ArrayList<>();


        for (Job job : jobs) {
            JobDTO companyDTO = new JobDTO();


            ExternalCompany company =
                            companyClient.getCompany(job.getCompanyId());
            log.info("Job with Company id: {} fetched", job.getCompanyId());

        List<ExternalReview> reviews = reviewClient.getReviews(job.getCompanyId());

            JobDTO jobDTO =
                    JobMapper.mapToJobWithCompanyDto(job, company, reviews);

                    dto.add(jobDTO);

        }
        return dto;
    }
//FallBack
    public List<String> companyBreakerFallback(Exception e){
        List<String> list = new ArrayList<>();
        list.add("Ooops!! Something went wrong...");
        return list;
    }




@RateLimiter(name = "companyBreaker")
    public String createJob(Job job){
        repo.save(job);
        log.info("Job created successfully");
        return "JOB ADDED SUCCESSFULLY";
    }

    public JobDTO findById(Long id) {
        Job job = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("JOB NOT  FOUND"));

        ExternalCompany company
                = companyClient.getCompany(job.getCompanyId());
     List<ExternalReview> reviews =
             reviewClient.getReviews(job.getCompanyId());

     JobDTO jobDTO = new JobDTO();
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

