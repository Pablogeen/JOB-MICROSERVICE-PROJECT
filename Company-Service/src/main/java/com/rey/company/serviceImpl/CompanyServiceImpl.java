package com.rey.company.serviceImpl;

import com.rey.company.clients.JobClient;
import com.rey.company.clients.ReviewClient;
import com.rey.company.dto.CompanyRequestDTO;
import com.rey.company.dto.CompanyResponseDTO;
import com.rey.company.dto.ErrorCodeEnum;
import com.rey.company.dto.ReviewMessage;
import com.rey.company.entity.Company;
import com.rey.company.exception.CompanyExceptionHandler;
import com.rey.company.exception.JobExceptionHandler;
import com.rey.company.exception.ReviewExceptionHandler;
import com.rey.company.repository.CompanyRepository;
import com.rey.company.service.ServiceInterface;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements ServiceInterface {

    private final CompanyRepository companyRepo;
    private final ReviewClient reviewClient;
    private final ModelMapper modelMapper;
    private final JobClient jobClient;

    @Override
    public List<CompanyResponseDTO> getAllCompanies() {
        log.info("Getting companies fro DB");

        List<Company> companies =
                companyRepo.findAll();

        log.info("Gotten companies from db: {}", companies);
        List<CompanyResponseDTO> mappedCompanies =
                companies.stream().
                        map(company -> modelMapper.map(company, CompanyResponseDTO.class))
                        .toList();
        log.info("Mapped companies to DTO: {}", mappedCompanies);
        return mappedCompanies;
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO company) {

        Company existingCompany = companyRepo.findById(id)
                .orElseThrow(() -> new CompanyExceptionHandler(
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));

        log.info("Retrieved company from the DB : {}", existingCompany);
        existingCompany.setName(company.getName());
        existingCompany.setDescription(company.getDescription());

        companyRepo.save(existingCompany);
        log.info("Updated Company saved in the DB: {}", existingCompany);

        CompanyResponseDTO mappedCompany =
                modelMapper.map(company, CompanyResponseDTO.class);
        log.info("Mapped company Entity into DTO: {}", mappedCompany);

        return mappedCompany;
    }

    @Override
    public String createCompany(CompanyRequestDTO company) {
        log.info("Received request to crate company");



        Company mappedCompany =
                modelMapper.map(company, Company.class);
        log.info("Mapped company DTO into ENTITY: {}", mappedCompany);

       boolean companyExist = companyRepo.findByCompanyName(mappedCompany.getName());
       log.info("Check if company exist: {}: ",companyExist);

       if (companyExist){
           throw new CompanyExceptionHandler(
                   ErrorCodeEnum.COMPANY_ALREADY_EXIST.getErrorCode(),
                   ErrorCodeEnum.COMPANY_ALREADY_EXIST.getErrorMessage(),
                   HttpStatus.CONFLICT
           );
       }
        companyRepo.save(mappedCompany);

        return "COMPANY CREATED SUCCESSFULLY";
    }

    @Override
    public CompanyResponseDTO getCompanyById(Long id) {

        Company company =
                companyRepo.findById(id)
                        .orElseThrow(() -> new CompanyExceptionHandler(
                                ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(),
                                ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorMessage(),
                                HttpStatus.NOT_FOUND
                        ));
        log.info("Retrieved company from the DB with id: {}", company.getId());

        CompanyResponseDTO mappedCompany =
                modelMapper.map(company, CompanyResponseDTO.class);
        log.info("Mapped company into DTO: {}", mappedCompany);

        return mappedCompany;
    }

    @Override
    @Transactional
    public String deleteCompany(Long companyId) {
        log.info("About to make call to delete reviews with companyId: {}", companyId);

        deleteReviewsWithCircuitBreaker(companyId);
        log.info("Reviews deleted with companyId: {}", companyId);

        log.info("About to make call to delete Job with companyId: {}", companyId);

        deleteJobsWithCircuitBreaker(companyId);
        log.info("Jobs deleted with companyId: {}", companyId);

        Company existingCompany = companyRepo.findById(companyId)
                .orElseThrow(() -> new CompanyExceptionHandler(
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));

        log.info("Retrieved existing company from the DB : {}", existingCompany);

        companyRepo.delete(existingCompany);
        log.info("Deleted successfully");

        return "COMPANY DELETED SUCCESSFULLY";
    }

    @CircuitBreaker(name = "UpdateCompanyRatingCB", fallbackMethod = "updateCompanyRatingFallback")
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        log.info("Updating the rating of company with id: {}", reviewMessage.getCompanyId());

        Company existingCompany = companyRepo.findById(reviewMessage.getCompanyId())
                .orElseThrow(() -> new CompanyExceptionHandler(
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));

        log.info("Gotten company with id: {}", existingCompany.getId());

        double averageRating = reviewClient.getAverageRating(reviewMessage.getCompanyId());
        log.info("Made a feign call to Reviews to update Rating: {}", averageRating);

        existingCompany.setRating(averageRating);
        companyRepo.save(existingCompany);
        log.info("Saved rating into the DB: {}", existingCompany.getRating());
    }

    public void updateCompanyRatingFallback(ReviewMessage reviewMessage, Throwable throwable) {
        log.error("CircuitBreaker fallback: Unable to update rating for companyId {}. Cause: {}",
                reviewMessage.getCompanyId(), throwable.getMessage());

        throw new CompanyExceptionHandler(
                ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorCode(),
                ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorMessage(),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }



    @CircuitBreaker(name = "reviewServiceCB", fallbackMethod = "deleteReviewsFallback")
    public void deleteReviewsWithCircuitBreaker(Long companyId) {
        reviewClient.deleteReviewsByCompanyId(companyId);
    }

    // Fallback method for reviewClient CircuitBreaker
    public void deleteReviewsFallback(Long companyId, Throwable throwable) {
        log.error("CircuitBreaker fallback: Unable to delete reviews for companyId {}. Cause: {}", companyId, throwable.getMessage());
        throw new ReviewExceptionHandler(
                ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorCode(),
                ErrorCodeEnum.REVIEW_SERVICE_UNAVAILABLE.getErrorMessage(),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @CircuitBreaker(name = "jobServiceCB", fallbackMethod = "deleteJobsFallback")
    public void deleteJobsWithCircuitBreaker(Long companyId) {
        jobClient.deleteJobsByCompanyId(companyId);
    }

    public void deleteJobsFallback(Long companyId, Throwable throwable) {
        log.error("CircuitBreaker fallback: Unable to delete jobs for companyId {}. Cause: {}", companyId, throwable.getMessage());
        throw new JobExceptionHandler(
                ErrorCodeEnum.JOB_SERVICE_UNAVAILABLE.getErrorCode(),
                ErrorCodeEnum.JOB_SERVICE_UNAVAILABLE.getErrorMessage(),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}