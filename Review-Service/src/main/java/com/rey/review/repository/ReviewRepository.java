package com.rey.review.repository;

import com.rey.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<List<Review>> findReviewsByCompanyId(Long companyId);

    void deleteAllByCompanyId(Long companyId);
}
