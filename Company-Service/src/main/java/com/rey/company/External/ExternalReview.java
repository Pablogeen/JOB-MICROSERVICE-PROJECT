package com.rey.company.External;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalReview {
    private Long id;
    private String title;
    private String description;
    private double rating;

}
