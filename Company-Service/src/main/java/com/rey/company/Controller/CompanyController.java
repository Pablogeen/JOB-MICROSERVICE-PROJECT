package com.rey.company.Controller;

import com.rey.company.ServiceImpl.CompanyServiceImpl;
import com.rey.company.Entity.Company;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private CompanyServiceImpl service;


    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies(){
        return new ResponseEntity<>(service.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") Long id){
        return new ResponseEntity<>(service.getCompanyById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable("id") Long id, @RequestBody Company company){
        return new ResponseEntity<>(service.updateCompany(id, company), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createCompany(@RequestBody Company company){
        return new ResponseEntity<>(service.createCompany(company), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable("id") Long id){
        return new ResponseEntity<>(service.deleteCompany(id), HttpStatus.NO_CONTENT);
    }
}
