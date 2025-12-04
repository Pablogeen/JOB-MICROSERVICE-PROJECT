package com.rey.review.Interface;

import com.rey.review.DTO.ReviewRequestDTO;
import com.rey.review.DTO.ReviewResponseDTO;

import java.util.List;

public interface ServiceInterface {
    List<ReviewResponseDTO> getAllReviewsByCompanyId(Long companyId);

    String addReview(Long companyId, ReviewRequestDTO reviewDTO);

    ReviewResponseDTO getReviewById(Long reviewId);

    ReviewResponseDTO updateReview(Long reviewId, ReviewRequestDTO review);

    String deleteReview(Long reviewId);

    Double getAverageRating(Long companyId);

    String deleteReviewsByCompanyId(Long companyId);
}
