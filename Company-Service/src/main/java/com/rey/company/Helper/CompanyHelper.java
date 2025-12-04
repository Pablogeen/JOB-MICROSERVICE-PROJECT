package com.rey.company.Helper;


import com.rey.company.DTO.CompanyRequestDTO;
import com.rey.company.DTO.ErrorCodeEnum;
import com.rey.company.Exception.CompanyServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CompanyHelper {


    public void validateCompanyRequest(CompanyRequestDTO company) {
        log.info("About to validate company request: {}",company);

        if (company.getName()== null || company.getName().isBlank() || company.getName().isEmpty()){
            log.info("Company Name must not be null and blank: {}",company.getName());
            throw new CompanyServiceException(
                    ErrorCodeEnum.INVALID_COMPANY_NAME.getErrorCode(),
                    ErrorCodeEnum.INVALID_COMPANY_NAME.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (company.getDescription() == null ||
                company.getDescription().isBlank() ||
                company.getDescription().isEmpty()){
            log.info("Company Description must not be null and blank: {}",company.getName());
            throw new CompanyServiceException(
                    ErrorCodeEnum.INVALID_COMPANY_DESCRIPTION.getErrorCode(),
                    ErrorCodeEnum.INVALID_COMPANY_DESCRIPTION.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }


    }
}
