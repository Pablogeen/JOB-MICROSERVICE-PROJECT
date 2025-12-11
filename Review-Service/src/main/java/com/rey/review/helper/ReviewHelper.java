package com.rey.review.helper;

import com.rey.review.constant.ErrorCodeEnum;
import com.rey.review.dto.ReviewRequestDTO;
import com.rey.review.exception.ReviewExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReviewHelper {

    public void validateRequest(ReviewRequestDTO reviewDTO) {

        if(reviewDTO.getDescription() == null || reviewDTO.getDescription().isBlank()){
            log.info("Handling null description");
            throw new ReviewExceptionHandler(
                    ErrorCodeEnum.INVALID_REQUEST.getErrorCode(),
                    ErrorCodeEnum.INVALID_REQUEST.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (reviewDTO.getTitle() == null || reviewDTO.getTitle().isBlank()){
            log.info("Title cannot be null or blank");
            throw new ReviewExceptionHandler(
                    ErrorCodeEnum.INVALID_REQUEST.getErrorCode(),
                    ErrorCodeEnum.INVALID_REQUEST.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (reviewDTO.getRating() == null || reviewDTO.getRating() < 0  || reviewDTO.getRating() > 5){
            throw new ReviewExceptionHandler(
                    ErrorCodeEnum.INVALID_RATING.getErrorCode(),
                    ErrorCodeEnum.INVALID_RATING.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }


    }
}
