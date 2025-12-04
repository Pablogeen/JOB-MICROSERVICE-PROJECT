package com.rey.company.Service;

import com.rey.company.DTO.CompanyRequestDTO;
import com.rey.company.DTO.CompanyResponseDTO;

import java.util.List;

public interface ServiceInterface {
    List<CompanyResponseDTO> getAllCompanies();

    CompanyResponseDTO getCompanyById(Long id);

    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO company);

    String createCompany(CompanyRequestDTO company);

    String deleteCompany(Long companyId);
}
