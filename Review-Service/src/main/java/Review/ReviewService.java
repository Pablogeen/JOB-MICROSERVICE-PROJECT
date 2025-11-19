package Review;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

   final private ReviewRepository repo;


    public List<Review> getAllReviews(Long companyId) {
        return repo.findReviewByCompanyId(companyId);
    }

    public String addReview(Long companyId, Review review) {


        review.setCompanyId(companyId);
        repo.save(review);
        return "REVIEW ADDED SUCCESSFULLY";
    }

    public Review getReviewById(Long reviewId) {
//        List<Review> reviews = repo.findReviewByCompanyId(companyId);
//
//        return reviews.stream()
//                .filter(review -> review.getId().equals(reviewId))
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("REVIEW NOT FOUND"));
  return repo.findById(reviewId)
          .orElseThrow(()-> new IllegalStateException("REVIEW NOT FOUND"));
   }

    public String updateReview( Long reviewId, Review review) {
        Review rev = repo.findById(reviewId)
                        .orElseThrow(()-> new IllegalStateException("REVIEW NOT FOUND"));

         rev.setDescription(review.getDescription());
         rev.setRating(review.getRating());
         rev.setTitle(review.getTitle());

            repo.save(rev);

            return "REVIEW UPDATED SUCCESSFULLY";
    }

    public String deleteReview(Long reviewId) {
        Review rev = repo.findById(reviewId)
                .orElseThrow(()-> new IllegalStateException("REVIEW NOT FOUND"));
        repo.delete(rev);
        return "REVIEW DELETED SUCCESSFULLY";
    }

    public Double getAverageRating(Long companyId) {
        List<Review> reviewList = repo.findReviewByCompanyId(companyId);
        return reviewList.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElseThrow(()-> new IllegalStateException("NO REVIEWS RECORDED"));
    }
}
