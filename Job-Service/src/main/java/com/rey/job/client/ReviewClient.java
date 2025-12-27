package com.rey.job.client;

import com.rey.job.external.ExternalReview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name ="REVIEW-SERVICE")
public interface ReviewClient {

    @GetMapping("/v1/reviews")
    List<ExternalReview> getAllReviews(@RequestParam("companyId") Long companyId);

}
