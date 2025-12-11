package com.rey.job.helper;

import com.rey.job.client.CompanyClient;
import com.rey.job.client.ReviewClient;
import com.rey.job.constants.ErrorCodeEnum;
import com.rey.job.entity.Job;
import com.rey.job.exception.CompanyHandlerException;
import com.rey.job.exception.ReviewHandlerException;
import com.rey.job.external.ExternalCompany;
import com.rey.job.external.ExternalReview;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalClients {

    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;

    @CircuitBreaker(name="companyBreaker", fallbackMethod = "companyFallBack")
    public ResponseEntity<ExternalCompany> makeCompanyCall(Job job) {
        try {
            ResponseEntity<ExternalCompany> company =
                    companyClient.getCompany(job.getCompanyId());
            log.info("Company found : {}", company);

            return company;

        } catch (FeignException.FeignClientException | FeignException.FeignServerException e) {
            // Handles 4xx and 5xx errors
            log.error("HTTP error response received from company service: {}", e.getMessage(), e);

            throw new CompanyHandlerException(
                    ErrorCodeEnum.COMPANY_SERVICE_UNAVAILABLE.getErrorCode(),
                    ErrorCodeEnum.COMPANY_SERVICE_UNAVAILABLE.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );

        } catch (FeignException e) {
            // Handles timeout, connection refused, decode errors, etc.
            log.error("Feign exception while calling company service: {}", e.getMessage(), e);

            throw new CompanyHandlerException(
                    ErrorCodeEnum.COMPANY_SERVICE_UNAVAILABLE.getErrorCode(),
                    ErrorCodeEnum.COMPANY_SERVICE_UNAVAILABLE.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );

        } catch (Exception e) {
            log.error("Unexpected exception while calling company: {}", e.getMessage(), e);

            throw new CompanyHandlerException(
                    ErrorCodeEnum.COMPANY_SERVICE_UNAVAILABLE.getErrorCode(),
                    ErrorCodeEnum.COMPANY_SERVICE_UNAVAILABLE.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }


    @CircuitBreaker(name="reviewBreaker", fallbackMethod = "reviewFallBack")
    public ResponseEntity<List<ExternalReview>> makeReviewCall(Job job) {
        try {
            ResponseEntity<List<ExternalReview>> reviews =
                    reviewClient.getAllReviews(job.getCompanyId());
            log.info("Reviews found : {}", reviews);

            return reviews;

        } catch (FeignException.FeignClientException | FeignException.FeignServerException e) {
            log.error("HTTP error response received from review service: {}", e.getMessage(), e);

            throw new CompanyHandlerException(
                    ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorCode(),
                    ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );

        } catch (FeignException e) {
            log.error("Feign exception while calling review service: {}", e.getMessage(), e);
            //Timeouts
            throw new CompanyHandlerException(
                    ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorCode(),
                    ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );

        } catch (Exception e) {
            log.error("Unexpected exception while calling review service: {}", e.getMessage(), e);

            throw new CompanyHandlerException(
                    ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorCode(),
                    ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }


    //Company Service FallBack
    public ResponseEntity<String> companyFallBack(Throwable t) {
        // Handle fallback logic here
        log.error("Fallback method called due to: {}", t.getMessage(), t);
        throw new CompanyHandlerException(
                ErrorCodeEnum.COMPANY_SERVICE_UNAVAILABLE.getErrorCode(),
                ErrorCodeEnum.COMPANY_SERVICE_UNAVAILABLE.getErrorMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

//Review Service FallBack
    public ResponseEntity<String> reviewFallBack(Throwable t) {
        // Handle fallback logic here
        log.error("Fallback method called due to: {}", t.getMessage(), t);
        throw new ReviewHandlerException(
                ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorCode(),
                ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}