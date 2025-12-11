package com.rey.company.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalJob {

    private String title;
    private String description;
    private Long minSalary;
    private Long maxSalary;
    private String location;
    private Long companyId;
}
