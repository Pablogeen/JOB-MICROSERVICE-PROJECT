package com.rey.review.serviceInterface;

import com.rey.review.dto.ReviewRequestDTO;
import com.rey.review.dto.ReviewResponseDTO;

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
