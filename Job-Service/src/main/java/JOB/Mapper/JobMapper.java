package JOB.Mapper;

import JOB.DTO.JobDTO;
import JOB.External.ExternalCompany;

import JOB.External.ExternalReview;
import JOB.Impl.Job;

import java.util.List;

public class JobMapper {

    public static JobDTO mapToJobWithCompanyDto(
            Job job,
            ExternalCompany company,
                    List<ExternalReview> reviews
    ){
        JobDTO jobDTO = new JobDTO();
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
