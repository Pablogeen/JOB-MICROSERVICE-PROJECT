package com.rey.job.Client;

import com.rey.job.External.ExternalReview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name ="REVIEW-SERVICE")
public interface ReviewClient {

    @GetMapping("/v1/reviews")
    ResponseEntity<List<ExternalReview>> getAllReviews(@RequestParam("companyId") Long companyId);

}
