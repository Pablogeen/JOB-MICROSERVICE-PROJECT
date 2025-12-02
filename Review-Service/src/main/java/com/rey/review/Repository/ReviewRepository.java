package com.rey.review.Repository;

import com.rey.review.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<List<Review>> findReviewsByCompanyId(Long companyId);

    void deleteAllByCompanyId(Long companyId);
}
