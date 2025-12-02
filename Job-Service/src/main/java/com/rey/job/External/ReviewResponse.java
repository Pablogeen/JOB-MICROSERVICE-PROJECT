package com.rey.job.External;

import lombok.Data;

import java.util.List;

@Data
public class ReviewResponse {

    List<ExternalReview> reviews;
}
