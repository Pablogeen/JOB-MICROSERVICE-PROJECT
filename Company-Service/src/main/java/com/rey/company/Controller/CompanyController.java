package com.rey.company.Controller;

import com.rey.company.DTO.CompanyDTO;
import com.rey.company.Service.ServiceInterface;
import com.rey.company.ServiceImpl.CompanyServiceImpl;
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
public class CompanyController {

    private final ServiceInterface serviceInterface;
    private final CompanyServiceImpl service;


    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies(){
        log.info("Retrieving all companies");
       List<CompanyDTO> companies = serviceInterface.getAllCompanies();
       log.info("Retrieved all companies: {}", companies);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable("id") Long id){
        log.info("Getting company with id: {}", id);
        CompanyDTO company = serviceInterface.getCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable("id") Long id,
                                                    @RequestBody CompanyDTO company){
        log.info("About to update company with id: {}", id);
        CompanyDTO updatedCompany = serviceInterface.updateCompany(id, company);
        log.info("Company has been updated: {}", updatedCompany);
        return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> createCompany(@RequestBody CompanyDTO company){
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
