package JOB.DTO;


import JOB.External.ExternalCompany;
import JOB.External.ExternalReview;
import lombok.Data;

import java.util.List;

@Data
public class JobDTO {

    private Long id;
    private String title;
    private String description;
    private String minSalary;
    private String maxSalary;
    private String location;
    private ExternalCompany company;
    private List<ExternalReview> review;

}