package com.rey.company.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "JOB-SERVICE")
public interface JobClient {

    @DeleteMapping("/v1/jobs/company/{companyId}")
     String deleteJobsByCompanyId(@PathVariable("companyId") Long companyId);

}
