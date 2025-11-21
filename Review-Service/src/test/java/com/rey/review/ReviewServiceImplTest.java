package com.rey.review.ServiceImpl;

import com.rey.review.Constant.ErrorCodeEnum;
import com.rey.review.DTO.ReviewDTO;
import com.rey.review.Entity.Review;
import com.rey.review.Exception.ReviewExceptionHandler;
import com.rey.review.Helper.ReviewHelper;
import com.rey.review.Repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
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

        Review review1 = new Review();
        Review review2 = new Review();

        ReviewDTO dto1 = new ReviewDTO();
        ReviewDTO dto2 = new ReviewDTO();

        when(reviewRepo.findReviewsByCompanyId(companyId))
                .thenReturn(Arrays.asList(review1, review2));

        when(modelMapper.map(review1, ReviewDTO.class)).thenReturn(dto1);
        when(modelMapper.map(review2, ReviewDTO.class)).thenReturn(dto2);

        List<ReviewDTO> result = reviewService.getAllReviewsByCompanyId(companyId);

        assertEquals(2, result.size());
        verify(reviewRepo, times(1)).findReviewsByCompanyId(companyId);
        verify(modelMapper, times(2)).map(any(), eq(ReviewDTO.class));
    }

    // ===============================================================
    // addReview
    // ===============================================================

    @Test
    void testAddReview_success() {
        Long companyId = 5L;

        ReviewDTO reviewDTO = new ReviewDTO();
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
        ReviewDTO dto = new ReviewDTO();

        when(reviewRepo.findById(reviewId)).thenReturn(Optional.of(review));
        when(modelMapper.map(review, ReviewDTO.class)).thenReturn(dto);

        ReviewDTO result = reviewService.getReviewById(reviewId);

        assertNotNull(result);
        verify(reviewRepo, times(1)).findById(reviewId);
        verify(modelMapper, times(1)).map(review, ReviewDTO.class);
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

        ReviewDTO updatedDTO = new ReviewDTO();
        updatedDTO.setDescription("Updated Desc");
        updatedDTO.setRating(4.5);
        updatedDTO.setTitle("Updated Title");

        ReviewDTO mappedDTO = new ReviewDTO();

        when(reviewRepo.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(modelMapper.map(existingReview, ReviewDTO.class)).thenReturn(mappedDTO);

        ReviewDTO result = reviewService.updateReview(reviewId, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Desc", existingReview.getDescription());
        assertEquals(4.5, existingReview.getRating());
        assertEquals("Updated Title", existingReview.getTitle());

        verify(reviewRepo, times(1)).save(existingReview);
        verify(modelMapper, times(1)).map(existingReview, ReviewDTO.class);
    }

    @Test
    void testUpdateReview_notFound() {
        Long reviewId = 50L;
        ReviewDTO dto = new ReviewDTO();

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

        Review r1 = new Review();
        r1.setRating(4.0);

        Review r2 = new Review();
        r2.setRating(2.0);

        when(reviewRepo.findReviewsByCompanyId(companyId))
                .thenReturn(Arrays.asList(r1, r2));

        Double avg = reviewService.getAverageRating(companyId);

        assertEquals(3.0, avg);
        assertNotNull(avg);
        verify(reviewRepo, times(1)).findReviewsByCompanyId(companyId);
    }

    @Test
    void testGetAverageRating_notFound() {
        Long companyId = 1L;

        when(reviewRepo.findReviewsByCompanyId(companyId))
                .thenReturn(List.of());

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewService.getAverageRating(companyId)
        );

        assertEquals(ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(), ex.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }
}
