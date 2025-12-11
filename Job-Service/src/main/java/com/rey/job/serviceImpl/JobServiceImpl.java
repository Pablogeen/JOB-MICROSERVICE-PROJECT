package com.rey.job.serviceImpl;

import com.rey.job.constants.ErrorCodeEnum;
import com.rey.job.dto.JobRequestDTO;
import com.rey.job.exception.JobExceptionHandler;
import com.rey.job.helper.CreateJobHelper;
import com.rey.job.helper.ExternalClients;
import com.rey.job.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import com.rey.job.dto.JobCompanyReviewDTO;
import com.rey.job.entity.Job;
import com.rey.job.external.ExternalCompany;
import com.rey.job.external.ExternalReview;
import com.rey.job.mapper.JobMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.rey.job.repository.JobRepository;
import com.rey.job.serviceInterface.JobService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository repo;

    private final ModelMapper modelMapper;
    private final CreateJobHelper jobHelper;
    private final ExternalClients externalClients;
    private final JsonUtil jsonUtil;

    @Override
    public List<JobCompanyReviewDTO> getAllJobsWithCompanyAndReview() {
        List<Job> jobs = repo.findAll();
        List<JobCompanyReviewDTO> dto = new ArrayList<>();
        for (Job job: jobs){
            log.info("Job with company id: {}",job.getCompanyId());

          ResponseEntity<ExternalCompany> company =
                  externalClients.makeCompanyCall(job);
          log.info("Made company request from Company Service: {}",company);

            ExternalCompany externalCompany = company.getBody();



            ResponseEntity<List<ExternalReview>> reviews =
                    externalClients.makeReviewCall(job);
            log.info("Made Review request from Review Service: {}",reviews);

            List<ExternalReview> externalReview = reviews.getBody();

            JobCompanyReviewDTO jobDTO =
                    JobMapper.mapToJobWithCompanyDto(job, externalCompany, externalReview);

            dto.add(jobDTO);
        }
        return dto;
    }


  //@Retry(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
  //  @RateLimiter(name="companyBreaker")


//FallBack
//    public List<String> companyBreakerFallback(Exception e){
//        List<String> list = new ArrayList<>();
//        list.add("Ooops!! Something went wrong...");
//        return list;
//    }




//@RateLimiter(name = "companyBreaker")
    @Override
    public String createJob(JobRequestDTO jobDTO){
        log.info("JobDTO received successfully in serviceImpl: {}",jobDTO);
        jobHelper.validateRequest(jobDTO);
        log.info("Validated job request");
    Job job =  modelMapper.map(jobDTO, Job.class);
    log.info("JobDTO mapped to JOB: {}",job);
    repo.save(job);
    log.info("Job saved into the database");
        return "JOB ADDED SUCCESSFULLY";
    }

    @Override
   public JobCompanyReviewDTO findById(Long id) {

        Job job = repo.findById(id)
                .orElseThrow(() -> new JobExceptionHandler(
                                ErrorCodeEnum.JOB_NOT_FOUND.getErrorCode(),
                            ErrorCodeEnum.JOB_NOT_FOUND.getErrorMessage(),
                                HttpStatus.NOT_FOUND
                        ));
        log.info("Fetching job from the db: {}",job);

        ResponseEntity<ExternalCompany> company
                = externalClients.makeCompanyCall(job);
        log.info("Making company request from Company Service: {}",company);

        ExternalCompany convertedCompany = company.getBody();

     ResponseEntity<List<ExternalReview>> reviews =
             externalClients.makeReviewCall(job);
        log.info("Making review request from Review Service: {}",reviews);

        List<ExternalReview> convertedReview = reviews.getBody();

        JobCompanyReviewDTO jobDTO =
                JobMapper.mapToJobWithCompanyDto(job, convertedCompany, convertedReview);
        log.info("Mapped job, company and convertedReview: {}",jobDTO);

        return jobDTO;
    }

    @Override
        public String deleteJobById(Long id){

        Job job = repo.findById(id)
                .orElseThrow(() -> new JobExceptionHandler(
                        ErrorCodeEnum.JOB_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.JOB_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));
        log.info("Fetched job from the db: {}",job);

          repo.delete(job);
          return "JOB DELETED SUCCESSFULLY";
        }

    @Override
    @Transactional
    public String deleteJobsByCompanyId(Long companyId) {
            repo.findAllJobsByCompanyId(companyId)
                    .orElseThrow(() -> new JobExceptionHandler(
                            ErrorCodeEnum.JOB_NOT_FOUND.getErrorCode(),
                            ErrorCodeEnum.JOB_NOT_FOUND.getErrorMessage(),
                            HttpStatus.NOT_FOUND
                    ));
            log.info("Fetched jobs with companyId: {} from DB",companyId);

            repo.deleteAllByCompanyId(companyId);
            log.info("All jobs with companyId: {} deleted", companyId);

        return "JOBS DELETED SUCCESSFULLY";
    }


    @Override
    public String updateJob(Long id, JobRequestDTO jobDto) {
            Job existingJob = repo.findById(id)
                    .orElseThrow(() -> new JobExceptionHandler(
                            ErrorCodeEnum.JOB_NOT_FOUND.getErrorCode(),
                            ErrorCodeEnum.JOB_NOT_FOUND.getErrorMessage(),
                            HttpStatus.NOT_FOUND
                    ));
            log.info("Got job from the DB: {}",existingJob);

            jobHelper.validateRequest(jobDto);
            log.info("Validating user request");

        existingJob.setTitle(jobDto.getTitle());
        existingJob.setDescription(jobDto.getDescription());
        existingJob.setMaxSalary(jobDto.getMaxSalary());
        existingJob.setMinSalary(jobDto.getMinSalary());
        existingJob.setLocation(jobDto.getLocation());
         repo.save(existingJob);
         return "JOB UPDATED SUCCESSFULLY";

    }



}

