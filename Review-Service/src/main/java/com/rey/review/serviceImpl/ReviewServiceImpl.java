package com.rey.review.serviceImpl;


import com.rey.review.constant.ErrorCodeEnum;
import com.rey.review.dto.ReviewRequestDTO;
import com.rey.review.dto.ReviewResponseDTO;
import com.rey.review.entity.Review;
import com.rey.review.exception.ReviewExceptionHandler;
import com.rey.review.helper.ReviewHelper;
import com.rey.review.serviceInterface.ServiceInterface;
import com.rey.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ServiceInterface {

    private final ReviewRepository reviewRepo;
    private final ModelMapper modelMapper;
    private final ReviewHelper reviewHelper;

    @Override
    public List<ReviewResponseDTO> getAllReviewsByCompanyId(Long companyId) {

        List<Review> reviewsByCompanyId =
                reviewRepo.findReviewsByCompanyId(companyId)
                        .orElseThrow(() -> new ReviewExceptionHandler(
                                ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                                ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                                HttpStatus.NOT_FOUND
                        ));
        log.info("Received reviews from the DB: {}", reviewsByCompanyId);
        List<ReviewResponseDTO> reviewDTO =
                reviewsByCompanyId.stream()
                        .map(reviews -> modelMapper.map(reviews, ReviewResponseDTO.class))
                        .toList();
        log.info("Converted reviews into reviewsDTO: {}", reviewDTO);
        return reviewDTO;
    }

    @Override
    public String addReview(Long companyId, ReviewRequestDTO reviewDTO) {
        //TODO: CHECK IF COMPANY EXIST...

        reviewHelper.validateRequest(reviewDTO);
        log.info("Validates users input");

        Review review =
                modelMapper.map(reviewDTO, Review.class);
        log.info("Converted reviewDTO to Review for inserting into DB: {}", review);

        review.setCompanyId(companyId);
        reviewRepo.save(review);
        log.info("Review has been saved successfully: {}", review);

        return "Review saved Successfully";
    }

    @Override
    public ReviewResponseDTO getReviewById(Long reviewId) {
//        List<Review> reviews = repo.findReviewByCompanyId(companyId);
//
//        return reviews.stream()
//                .filter(review -> review.getId().equals(reviewId))
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("REVIEW NOT FOUND"));
  Review review = reviewRepo.findById(reviewId)
          .orElseThrow(()-> new ReviewExceptionHandler(
                  ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                  ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                  HttpStatus.NOT_FOUND
          ));
        ReviewResponseDTO mappedReview =
                 modelMapper.map(review, ReviewResponseDTO.class);

    return mappedReview;
   }

   @Override
    public ReviewResponseDTO updateReview(Long reviewId, ReviewRequestDTO review) {
        Review rev = reviewRepo.findById(reviewId)
                .orElseThrow(()-> new ReviewExceptionHandler(
                        ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));

         rev.setDescription(review.getDescription());
         rev.setRating(review.getRating());
         rev.setTitle(review.getTitle());

       reviewRepo.save(rev);

       ReviewResponseDTO updatedRev =
                 modelMapper.map(rev, ReviewResponseDTO.class);

            return updatedRev;
    }


    @Override
    @Transactional
    public String deleteReview(Long reviewId) {
        Review rev = reviewRepo.findById(reviewId)
                .orElseThrow(()-> new ReviewExceptionHandler(
                        ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));
        reviewRepo.delete(rev);

        return "REVIEW DELETED SUCCESSFULLY";
    }

    @Override
    @Transactional
    public String deleteReviewsByCompanyId(Long companyId) {

        List<Review> review =
                reviewRepo.findReviewsByCompanyId(companyId)
                        .orElseThrow(() -> new ReviewExceptionHandler(
                                ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                                ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                                HttpStatus.NOT_FOUND
                        ));
        log.info("Gotten all reviews with companyId: {}",companyId);

        reviewRepo.deleteAllByCompanyId(companyId);
        log.info("Deleted all reviews with companyId: {}",companyId);
        return "REVIEWS DELETED SUCCESSFULLY";
    }

    @Override
    public Double getAverageRating(Long companyId) {
        List<Review> reviewList =
                reviewRepo.findReviewsByCompanyId(companyId)
                        .orElseThrow(() -> new ReviewExceptionHandler(
                                ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                                ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                                HttpStatus.NOT_FOUND
                        ));
        log.info("Retrieved reviews by companyId: {}",reviewList);
        return reviewList.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElseThrow(()-> new ReviewExceptionHandler(
                        ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));
    }




}
