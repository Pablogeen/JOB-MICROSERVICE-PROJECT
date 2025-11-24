package com.rey.company.Service;

import com.rey.company.DTO.CompanyDTO;

import java.util.List;

public interface ServiceInterface {
    List<CompanyDTO> getAllCompanies();

    CompanyDTO getCompanyById(Long id);

    CompanyDTO updateCompany(Long id, CompanyDTO company);

    String createCompany(CompanyDTO company);

    String deleteCompany(Long id);
}
