package com.rey.company.Helper;

import com.rey.company.Clients.JobClient;
import com.rey.company.Clients.ReviewClient;
import com.rey.company.DTO.ErrorCodeEnum;
import com.rey.company.Exception.CompanyServiceException;
import com.rey.company.External.ExternalReview;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalClientHelper {

    private final JobClient jobClient;
    private final ReviewClient reviewClient;


   // @CircuitBreaker(name="companyBreaker", fallbackMethod = "companyFallBack")
    public String makeDeleteReviewCall(Long companyId) {
        try {
                    reviewClient.deleteReviewsByCompanyId(companyId);
            log.info("Reviews deleted");

            return "CALL MADE TO DELETE REVIEWS SUCCESSFULLY";

        } catch (FeignException.FeignClientException | FeignException.FeignServerException e) {
            // Handles 4xx and 5xx errors
            log.error("HTTP error response received from review service: {}", e.getMessage(), e);

            throw new CompanyServiceException(
                ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                    ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );

        } catch (FeignException e) {
            // Handles timeout, connection refused, decode errors, etc.
            log.error("Feign exception while calling review service: {}", e.getMessage(), e);

            throw new CompanyServiceException(
                    ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                    ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );

        } catch (Exception e) {
            log.error("Unexpected exception while calling review: {}", e.getMessage(), e);

            throw new CompanyServiceException(
                    ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorCode(),
                    ErrorCodeEnum.REVIEW_NOT_FOUND.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }

    // @CircuitBreaker(name="companyBreaker", fallbackMethod = "companyFallBack")
    public String makeDeleteJobsCall(Long companyId) {
        try {
                    jobClient.deleteJobsByCompanyId(companyId);
            log.info(" Jobs deleted with companyId : {}", companyId);

            return "CALL MADE TO DELETE JOBS SUCCESSFULLY";

        } catch (FeignException.FeignClientException | FeignException.FeignServerException e) {
            // Handles 4xx and 5xx errors
            log.error("HTTP error response received from review service: {}", e.getMessage(), e);

            throw new CompanyServiceException(
                    ErrorCodeEnum.JOB_NOT_FOUND.getErrorCode(),
                    ErrorCodeEnum.JOB_NOT_FOUND.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );

        } catch (FeignException e) {
            // Handles timeout, connection refused, decode errors, etc.
            log.error("Feign exception while calling review service: {}", e.getMessage(), e);

            throw new CompanyServiceException(
                    ErrorCodeEnum.JOB_NOT_FOUND.getErrorCode(),
                    ErrorCodeEnum.JOB_NOT_FOUND.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );

        } catch (Exception e) {
            log.error("Unexpected exception while calling review: {}", e.getMessage(), e);

            throw new CompanyServiceException(
                    ErrorCodeEnum.JOB_NOT_FOUND.getErrorCode(),
                    ErrorCodeEnum.JOB_NOT_FOUND.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }

}
