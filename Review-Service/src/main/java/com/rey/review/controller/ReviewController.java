package com.rey.review.controller;

import com.rey.review.dto.ReviewRequestDTO;
import com.rey.review.dto.ReviewResponseDTO;
import com.rey.review.serviceInterface.ServiceInterface;
import com.rey.review.messaging.ReviewMessageProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "REVIEW")
public class ReviewController {

     private final ServiceInterface serviceInterface;
     private ReviewMessageProducer messageProducer;

    @Operation(
            description = "Get Endpoint for Review",
            summary = "This is a summary for Review get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews(
            @RequestParam("companyId") Long companyId){
        log.info("About to to make request for all reviews");
        List<ReviewResponseDTO> reviewDTO = serviceInterface.getAllReviewsByCompanyId(companyId);
        log.info("Request successfully made: {}", reviewDTO);
        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @PostMapping("/add-review")
    public ResponseEntity<String> addReview(@RequestParam("companyId") Long companyId,
                                            @RequestBody ReviewRequestDTO reviewDTO){
        log.info("Receiving request to add Review with CompanyId: {}||{}", reviewDTO, companyId);
        String response = serviceInterface.addReview(companyId, reviewDTO);
        //messageProducer.sendMessage(reviewDTO);
        log.info("Review has been added: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

                                                          @GetMapping("/{reviewId}")
                                                          public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable("reviewId") Long reviewId){
        log.info("Getting review with id: {}",reviewId);
        ReviewResponseDTO reviewById = serviceInterface.getReviewById(reviewId);
        log.info("Review by Id gotten: {}",reviewById);
        return new ResponseEntity<>(reviewById, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable("reviewId") Long reviewId,
                                                          @RequestBody ReviewRequestDTO review){
        log.info("About to update review with id and ReviewDTO {}||{}", reviewId, review);
        ReviewResponseDTO updatedReview = serviceInterface.updateReview(reviewId, review);
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
