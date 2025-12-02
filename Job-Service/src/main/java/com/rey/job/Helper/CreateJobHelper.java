package com.rey.job.Helper;

import com.rey.job.Constants.ErrorCodeEnum;
import com.rey.job.DTO.JobDTO;
import com.rey.job.Exception.JobExceptionHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CreateJobHelper {


    public void validateRequest(JobDTO request) {

        // Title
        if (request.getTitle() == null || request.getTitle().strip().isEmpty()
                || request.getTitle().length() < 2 || request.getTitle().length() > 100) {
            log.info("Invalid title");
            throw new JobExceptionHandler(
                    ErrorCodeEnum.INVALID_REQUEST.getErrorCode(),
                    ErrorCodeEnum.INVALID_REQUEST.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Description
        if (request.getDescription() == null || request.getDescription().strip().isEmpty()
                || request.getDescription().length() < 10 || request.getDescription().length() > 500) {
            log.info("Invalid Description");
            throw new JobExceptionHandler(
                    ErrorCodeEnum.INVALID_DESCRIPTION.getErrorCode(),
                    ErrorCodeEnum.INVALID_DESCRIPTION.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Minimum salary
        if (request.getMinSalary() == null || request.getMinSalary() < 0) {
            log.info("Validating MinSalary");
            throw new JobExceptionHandler(
                    ErrorCodeEnum.INVALID_SALARY.getErrorCode(),
                    ErrorCodeEnum.INVALID_SALARY.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Maximum salary
        if (request.getMaxSalary() == null || request.getMaxSalary() <= 0) {
            log.info("Validating MaxSalary");
            throw new JobExceptionHandler(
                    ErrorCodeEnum.INVALID_SALARY.getErrorCode(),
                    ErrorCodeEnum.INVALID_SALARY.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

            // Compare salaries
            if (request.getMinSalary() >= request.getMaxSalary()) {
                throw new JobExceptionHandler(
                        ErrorCodeEnum.INVALID_SALARY.getErrorCode(),
                        ErrorCodeEnum.INVALID_SALARY.getErrorMessage(),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Location
            if (request.getLocation() == null || request.getLocation().strip().isEmpty()) {
                throw new JobExceptionHandler(
                        ErrorCodeEnum.INVALID_LOCATION.getErrorCode(),
                        ErrorCodeEnum.INVALID_LOCATION.getErrorMessage(),
                        HttpStatus.BAD_REQUEST
                );
            }


        }

    }


