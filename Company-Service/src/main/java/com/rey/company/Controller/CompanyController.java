package com.rey.company.Controller;

import com.rey.company.DTO.CompanyRequestDTO;
import com.rey.company.DTO.CompanyResponseDTO;
import com.rey.company.Service.ServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/companies")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Company")
public class CompanyController {

    private final ServiceInterface serviceInterface;


    @Operation(
            description = "Get Endpoint for Company",
            summary = "This is a summary for Company get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> getAllCompanies(){
        log.info("Retrieving all companies");
       List<CompanyResponseDTO> companies = serviceInterface.getAllCompanies();
       log.info("Retrieved all companies: {}", companies);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable("id") Long id){
        log.info("Getting company with id: {}", id);
        CompanyResponseDTO company = serviceInterface.getCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(@PathVariable("id") Long id,
                                                           @RequestBody CompanyRequestDTO company){
        log.info("About to update company with id: {}", id);
        CompanyResponseDTO updatedCompany = serviceInterface.updateCompany(id, company);
        log.info("Company has been updated: {}", updatedCompany);
        return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> createCompany(@RequestBody CompanyRequestDTO company){
        log.info("Request to create a company: {}",company);
        String creationResponse = serviceInterface.createCompany(company);
        log.info("Company has been created");
        return new ResponseEntity<>(creationResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<String> deleteCompany(@PathVariable("companyId") Long companyId){
        log.info("About to delete company with id: {}",companyId);
        String deletedResponse = serviceInterface.deleteCompany(companyId);
        return new ResponseEntity<>(deletedResponse, HttpStatus.NO_CONTENT);
    }
}
