package com.rey.job.external;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ExternalCompany {
    private Long id;
    private String name;
    private String description;

}
