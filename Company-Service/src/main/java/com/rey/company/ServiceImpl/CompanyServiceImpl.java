package com.rey.company.ServiceImpl;

import com.rey.company.Clients.ReviewClient;
import com.rey.company.DTO.CompanyRequestDTO;
import com.rey.company.DTO.CompanyResponseDTO;
import com.rey.company.DTO.ErrorCodeEnum;
import com.rey.company.DTO.ReviewMessage;

import com.rey.company.Entity.Company;
import com.rey.company.Exception.CompanyServiceException;
import com.rey.company.Helper.CompanyHelper;
import com.rey.company.Helper.ExternalClientHelper;
import com.rey.company.Repository.CompanyRepository;
import com.rey.company.Service.ServiceInterface;
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
    private final CompanyHelper companyHelper;
    private final ExternalClientHelper clientHelper;

    @Override
    public List<CompanyResponseDTO> getAllCompanies() {
        log.info("Getting companies fro DB");

        List<Company> companies =
                companyRepo.findAll();

        log.info("Gotten companies from db: {}",companies);
        List<CompanyResponseDTO>  mappedCompanies =
                companies.stream().
                        map(company -> modelMapper.map(company, CompanyResponseDTO.class))
                        .toList();
        log.info("Mapped companies to DTO: {}", mappedCompanies);
        return mappedCompanies;
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO company) {

        Company existingCompany = companyRepo.findById(id)
                  .orElseThrow(()-> new CompanyServiceException(
                ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(),
                ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorMessage(),
                HttpStatus.NOT_FOUND
        ));

        log.info("Retrieved company from the DB : {}", existingCompany);
        existingCompany.setName(company.getName());
        existingCompany.setDescription(company.getDescription());

        companyRepo.save(existingCompany);
        log.info("Updated Company saved in the DB: {}",existingCompany);

        CompanyResponseDTO mappedCompany =
                modelMapper.map(company, CompanyResponseDTO.class);
        log.info("Mapped company Entity into DTO: {}", mappedCompany);

        return mappedCompany;
    }

    @Override
    public String createCompany(CompanyRequestDTO company) {
        log.info("Received request to crate company");

        companyHelper.validateCompanyRequest(company);
        log.info("Validated company request");

        Company mappedCompany =
                modelMapper.map(company, Company.class);
        log.info("Mapped company DTO into ENTITY: {}", mappedCompany);

        companyRepo.save(mappedCompany);

        return "COMPANY CREATED SUCCESSFULLY";
    }

    @Override
    public CompanyResponseDTO getCompanyById(Long id) {

        Company company =
                companyRepo.findById(id)
                        .orElseThrow(()-> new CompanyServiceException(
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
        log.info("About to make call to delete reviews with companyId: {}",companyId);
        clientHelper.makeDeleteReviewCall(companyId);
        log.info("Reviews deleted with companyId: {}",companyId);

        log.info("About to make call to delete Job with companyId: {}",companyId);
        clientHelper.makeDeleteJobsCall(companyId);
        log.info("Jobs deleted with companyId: {}",companyId);

        Company existingCompany = companyRepo.findById(companyId)
                .orElseThrow(()-> new CompanyServiceException(
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));

        log.info("Retrieved existing company from the DB : {}", existingCompany);

        companyRepo.delete(existingCompany);
        log.info("Deleted successfully");
        return  "COMPANY DELETED SUCCESSFULLY";
    }

    public void updateCompanyRating(ReviewMessage reviewMessage){
        log.info("Updating the rating of company with id: {}",reviewMessage.getId());
        Company existingCompany = companyRepo.findById(reviewMessage.getCompanyId())
                .orElseThrow(()-> new CompanyServiceException(
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));
        log.info("Gotten company with id: {}",existingCompany.getId());

            double averageRating = reviewClient.getAverageRating(reviewMessage.getCompanyId());
            log.info("Made a feign call to Reviews to update Rating: {}",averageRating);
        existingCompany.setRating(averageRating);

            companyRepo.save(existingCompany);
            log.info("Saved rating into the DB: {}",existingCompany.getRating());
    }
}
