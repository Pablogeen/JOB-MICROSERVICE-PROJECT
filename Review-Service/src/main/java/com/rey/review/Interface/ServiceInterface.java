package com.rey.review.Interface;

import com.rey.review.DTO.ReviewDTO;

import java.util.List;

public interface ServiceInterface {
    List<ReviewDTO> getAllReviewsByCompanyId(Long companyId);

    String addReview(Long companyId, ReviewDTO reviewDTO);

    ReviewDTO getReviewById(Long reviewId);

    ReviewDTO updateReview(Long reviewId, ReviewDTO review);

    String deleteReview(Long reviewId);

    Double getAverageRating(Long companyId);
}
