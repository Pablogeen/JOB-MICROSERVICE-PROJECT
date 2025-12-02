package com.rey.review.Controller;

import com.rey.review.DTO.ReviewDTO;
import com.rey.review.Interface.ServiceInterface;
import com.rey.review.Messaging.ReviewMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

     private final ServiceInterface serviceInterface;
     private ReviewMessageProducer messageProducer;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews(@RequestParam("companyId") Long companyId){
        log.info("About to to make request for all reviews");
        List<ReviewDTO> reviewDTO = serviceInterface.getAllReviewsByCompanyId(companyId);
        log.info("Request successfully made: {}", reviewDTO);
        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @PostMapping("/add-review")
    public ResponseEntity<String> addReview(@RequestParam("companyId") Long companyId,
                                            @RequestBody ReviewDTO reviewDTO){
        log.info("Receiving request to add Review with CompanyId: {}||{}", reviewDTO, companyId);
        String response = serviceInterface.addReview(companyId, reviewDTO);
        //messageProducer.sendMessage(reviewDTO);
        log.info("Review has been added: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable("reviewId") Long reviewId){
        log.info("Getting review with id: {}",reviewId);
        ReviewDTO reviewById = serviceInterface.getReviewById(reviewId);
        log.info("Review by Id gotten: {}",reviewById);
        return new ResponseEntity<>(reviewById, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable("reviewId") Long reviewId,
                                                  @RequestBody ReviewDTO review){
        log.info("About to update review with id and ReviewDTO {}||{}", reviewId, review);
        ReviewDTO updatedReview = serviceInterface.updateReview(reviewId, review);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long reviewId){
        log.info("Received reviewId to deleteReview: {}",reviewId);
        String deletedReview = serviceInterface.deleteReview(reviewId);
        log.info("Deleted review with id: {}", reviewId);
        return new ResponseEntity<>(deletedReview, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/company/{companyId}")
    public ResponseEntity<String> deleteReviewsByCompanyId(@PathVariable("companyId") Long companyId){
        log.info("Received companyId to deleteReview: {}",companyId);
        String deletedReview = serviceInterface.deleteReviewsByCompanyId(companyId);
        log.info("Deleted review with companyId: {}", companyId);
        return new ResponseEntity<>(deletedReview, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/average-rating")
    public ResponseEntity<Double> getAverageRating(@RequestParam("companyId") Long companyId){
        log.info("Getting the average rating of company with id:{}",companyId);
        Double averageRating = serviceInterface.getAverageRating(companyId);
        log.info("Calculated averageRating: {}",averageRating);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }


}
