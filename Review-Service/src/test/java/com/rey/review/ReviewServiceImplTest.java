package com.rey.review.serviceImpl;

import com.rey.review.constant.ErrorCodeEnum;
import com.rey.review.dto.ReviewRequestDTO;
import com.rey.review.dto.ReviewResponseDTO;
import com.rey.review.entity.Review;
import com.rey.review.exception.ReviewExceptionHandler;
import com.rey.review.helper.ReviewHelper;
import com.rey.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ReviewHelper reviewHelper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    // ===============================================================
    // getAllReviewsByCompanyId
    // ===============================================================

    @Test
    void testGetAllReviewsByCompanyId_success() {
        Long companyId = 1L;

            List<Review> reviewList = new ArrayList<>();
        Review review1 = new Review();
        Review review2 = new Review();

        reviewList.add(review1);
        reviewList.add(review2);

        ReviewRequestDTO dto1 = new ReviewRequestDTO();
        ReviewRequestDTO dto2 = new ReviewRequestDTO();

        ReviewResponseDTO responseDto1 = new ReviewResponseDTO();
        ReviewResponseDTO responseDto2 = new ReviewResponseDTO();

        when(reviewRepo.findReviewsByCompanyId(companyId))
                .thenReturn(Optional.of(reviewList));

        when(modelMapper.map(review1, ReviewRequestDTO.class)).thenReturn(dto1);
        when(modelMapper.map(review2, ReviewRequestDTO.class)).thenReturn(dto2);

        List<ReviewResponseDTO> result = reviewService.getAllReviewsByCompanyId(companyId);

        assertEquals(2, result.size());
        verify(reviewRepo, times(1)).findReviewsByCompanyId(companyId);
        verify(modelMapper, times(2)).map(any(), eq(ReviewRequestDTO.class));
    }

    // ===============================================================
    // addReview
    // ===============================================================

    @Test
    void testAddReview_success() {
        Long companyId = 5L;

        ReviewRequestDTO reviewDTO = new ReviewRequestDTO();
        Review mappedReview = new Review();

        doNothing().when(reviewHelper).validateRequest(reviewDTO);
        when(modelMapper.map(reviewDTO, Review.class)).thenReturn(mappedReview);

        String response = reviewService.addReview(companyId, reviewDTO);

        assertEquals("Review saved Successfully", response);

        assertEquals(companyId, mappedReview.getCompanyId());

        verify(reviewHelper, times(1)).validateRequest(reviewDTO);
        verify(modelMapper, times(1)).map(reviewDTO, Review.class);
        verify(reviewRepo, times(1)).save(mappedReview);
    }

    // ===============================================================
    // getReviewById
    // ===============================================================

    @Test
    void testGetReviewById_success() {
        Long reviewId = 10L;

        Review review = new Review();
        ReviewRequestDTO dto = new ReviewRequestDTO();

        when(reviewRepo.findById(reviewId)).thenReturn(Optional.of(review));
        when(modelMapper.map(review, ReviewRequestDTO.class)).thenReturn(dto);

        ReviewResponseDTO result = reviewService.getReviewById(reviewId);

        assertNotNull(result);
        verify(reviewRepo, times(1)).findById(reviewId);
        verify(modelMapper, times(1)).map(review, ReviewRequestDTO.class);
    }

    @Test
    void testGetReviewById_notFound() {
        Long reviewId = 99L;

        when(reviewRepo.findById(reviewId)).thenReturn(Optional.empty());

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewService.getReviewById(reviewId)
        );

        assertEquals(ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(), ex.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    // ===============================================================
    // updateReview
    // ===============================================================

    @Test
    void testUpdateReview_success() {
        Long reviewId = 100L;

        Review existingReview = new Review();
        existingReview.setId(reviewId);

        ReviewRequestDTO updatedDTO = new ReviewRequestDTO();
        updatedDTO.setDescription("Updated Desc");
        updatedDTO.setRating(4.5);
        updatedDTO.setTitle("Updated Title");

        ReviewRequestDTO mappedDTO = new ReviewRequestDTO();

        when(reviewRepo.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(modelMapper.map(existingReview, ReviewRequestDTO.class)).thenReturn(mappedDTO);

        ReviewResponseDTO result = reviewService.updateReview(reviewId, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Desc", existingReview.getDescription());
        assertEquals(4.5, existingReview.getRating());
        assertEquals("Updated Title", existingReview.getTitle());

        verify(reviewRepo, times(1)).save(existingReview);
        verify(modelMapper, times(1)).map(existingReview, ReviewRequestDTO.class);
    }

    @Test
    void testUpdateReview_notFound() {
        Long reviewId = 50L;
        ReviewRequestDTO dto = new ReviewRequestDTO();

        when(reviewRepo.findById(reviewId)).thenReturn(Optional.empty());

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewService.updateReview(reviewId, dto)
        );

        assertEquals(ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(), ex.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    // ===============================================================
    // deleteReview
    // ===============================================================

    @Test
    void testDeleteReview_success() {
        Long reviewId = 77L;

        Review review = new Review();
        when(reviewRepo.findById(reviewId)).thenReturn(Optional.of(review));

        String result = reviewService.deleteReview(reviewId);

        assertEquals("REVIEW DELETED SUCCESSFULLY", result);

        verify(reviewRepo, times(1)).delete(review);
    }

    @Test
    void testDeleteReview_notFound() {
        Long reviewId = 88L;

        when(reviewRepo.findById(reviewId)).thenReturn(Optional.empty());

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewService.deleteReview(reviewId)
        );

        assertEquals(ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(), ex.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    // ===============================================================
    // getAverageRating
    // ===============================================================

    @Test
    void testGetAverageRating_success() {
        Long companyId = 1L;

        List<Review> reviewList = new ArrayList<>();
        Review r1 = new Review();
        r1.setRating(4.0);

        Review r2 = new Review();
        r2.setRating(2.0);

        reviewList.add(r1);
        reviewList.add(r2);

        when(reviewRepo.findReviewsByCompanyId(companyId))
                .thenReturn(Optional.of(reviewList));

        Double avg = reviewService.getAverageRating(companyId);

        assertEquals(3.0, avg);
        assertNotNull(avg);
        verify(reviewRepo, times(1)).findReviewsByCompanyId(companyId);
    }

    @Test
    void testGetAverageRating_notFound() {
        Long companyId = 1L;

        List<Review> reviewList = new ArrayList<>();

        when(reviewRepo.findReviewsByCompanyId(companyId))
                .thenReturn(Optional.of(reviewList));

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewService.getAverageRating(companyId)
        );

        assertEquals(ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(), ex.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }
}
