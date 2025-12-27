package com.rey.review.client;

import com.rey.review.external.ExternalCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "REVIEW-CLIENT")
public interface CompanyClient {

    @GetMapping("/v1/companies/{id}")
    ExternalCompany getCompanyById(@PathVariable("id") Long id);
}
