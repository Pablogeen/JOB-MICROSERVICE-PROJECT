package com.rey.job.entity;

import java.lang.Long;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Long minSalary;
    private Long maxSalary;
    private String location;
    private Long companyId;

}