package com.rey.company.service;

import com.rey.company.dto.CompanyRequestDTO;
import com.rey.company.dto.CompanyResponseDTO;

import java.util.List;

public interface ServiceInterface {
    List<CompanyResponseDTO> getAllCompanies();

    CompanyResponseDTO getCompanyById(Long id);

    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO company);

    String createCompany(CompanyRequestDTO company);

    String deleteCompany(Long companyId);
}
