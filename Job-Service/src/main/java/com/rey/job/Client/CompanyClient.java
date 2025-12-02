package com.rey.job.Client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rey.job.External.ExternalCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "COMPANY-SERVICE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CompanyClient {


        @GetMapping("/v1/companies/{id}")
        ResponseEntity<ExternalCompany> getCompany (@PathVariable("id") Long id);




}
