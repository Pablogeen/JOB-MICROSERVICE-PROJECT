package com.rey.review.helper;

import com.rey.review.constant.ErrorCodeEnum;
import com.rey.review.dto.ReviewRequestDTO;
import com.rey.review.exception.ReviewExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReviewHelperTest {

    private final ReviewHelper reviewHelper = new ReviewHelper();

    // ==========================================================
    // Description Tests
    // ==========================================================

    @Test
    void testValidateRequest_descriptionNull() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setDescription(null);
        dto.setTitle("Good");
        dto.setRating(4.0);

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewHelper.validateRequest(dto)
        );

        assertEquals(ErrorCodeEnum.INVALID_REQUEST.getErrorCode(), ex.getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void testValidateRequest_descriptionBlank() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setDescription("   ");
        dto.setTitle("Good");
        dto.setRating(4.0);

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewHelper.validateRequest(dto)
        );

        assertEquals(ErrorCodeEnum.INVALID_REQUEST.getErrorCode(), ex.getErrorCode());
    }

    // ==========================================================
    // Title Tests
    // ==========================================================

    @Test
    void testValidateRequest_titleNull() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setDescription("Nice review");
        dto.setTitle(null);
        dto.setRating(4.0);

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewHelper.validateRequest(dto)
        );

        assertEquals(ErrorCodeEnum.INVALID_REQUEST.getErrorCode(), ex.getErrorCode());
        assertNotNull(ex);
        assertEquals(ErrorCodeEnum.INVALID_REQUEST.getErrorMessage(), ex.getErrorMessage());
    }

    @Test
    void testValidateRequest_titleBlank() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setDescription("Nice review");
        dto.setTitle("   ");
        dto.setRating(4.0);

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewHelper.validateRequest(dto)
        );

        assertEquals(ErrorCodeEnum.INVALID_REQUEST.getErrorCode(), ex.getErrorCode());
    }

    // ==========================================================
    // Rating Tests
    // ==========================================================

    @Test
    void testValidateRequest_ratingNull() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setDescription("desc");
        dto.setTitle("title");
        dto.setRating(null);

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewHelper.validateRequest(dto)
        );

        assertEquals(ErrorCodeEnum.INVALID_RATING.getErrorCode(), ex.getErrorCode());
    }

    @Test
    void testValidateRequest_ratingLessThanZero() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setDescription("desc");
        dto.setTitle("title");
        dto.setRating(-1.0);

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewHelper.validateRequest(dto)
        );

        assertEquals(ErrorCodeEnum.INVALID_RATING.getErrorCode(), ex.getErrorCode());
    }

    @Test
    void testValidateRequest_ratingGreaterThanFive() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setDescription("desc");
        dto.setTitle("title");
        dto.setRating(6.0);

        ReviewExceptionHandler ex = assertThrows(
                ReviewExceptionHandler.class,
                () -> reviewHelper.validateRequest(dto)
        );

        assertEquals(ErrorCodeEnum.INVALID_RATING.getErrorCode(), ex.getErrorCode());
    }

    // ==========================================================
    // Valid Case
    // ==========================================================

    @Test
    void testValidateRequest_validRequest() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setDescription("Good review");
        dto.setTitle("Excellent");
        dto.setRating(4.5);

        // Should NOT throw an exception
        assertDoesNotThrow(() -> reviewHelper.validateRequest(dto));
    }
}
