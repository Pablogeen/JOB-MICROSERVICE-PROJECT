package com.rey.job.external;

import lombok.Data;

import java.util.List;

@Data
public class ReviewResponse {

    List<ExternalReview> reviews;
}
