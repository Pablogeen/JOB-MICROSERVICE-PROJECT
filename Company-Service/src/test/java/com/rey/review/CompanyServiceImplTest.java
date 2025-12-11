package com.rey.company.serviceImpl;

import com.rey.company.clients.ReviewClient;
import com.rey.company.dto.CompanyRequestDTO;
import com.rey.company.dto.CompanyResponseDTO;
import com.rey.company.dto.ErrorCodeEnum;
import com.rey.company.dto.ReviewMessage;
import com.rey.company.entity.Company;
import com.rey.company.exception.CompanyServiceException;
import com.rey.company.helper.CompanyHelper;
import com.rey.company.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Mock
    private CompanyRepository companyRepo;

    @Mock
    private ReviewClient reviewClient;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CompanyHelper companyHelper;

    private Company company;
    private CompanyRequestDTO companyDTO;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setName("Test Company");
        company.setDescription("Test Description");
        company.setRating(4.5);

        companyDTO = new CompanyRequestDTO();
        companyDTO.setName("Test Company");
        companyDTO.setDescription("Test Description");

    }

    @Test
    void testGetAllCompanies() {
        when(companyRepo.findAll()).thenReturn(List.of(company));
        when(modelMapper.map(company, CompanyRequestDTO.class)).thenReturn(companyDTO);

        List<CompanyResponseDTO> result = companyService.getAllCompanies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Company", result.get(0).getName());
        assertEquals("Test Description", result.get(0).getDescription());

        verify(companyRepo, times(1)).findAll();
        verify(modelMapper, times(1)).map(company, CompanyRequestDTO.class);
    }

    @Test
    void testUpdateCompany_Success() {
        when(companyRepo.findById(1L)).thenReturn(Optional.of(company));
        when(modelMapper.map(companyDTO, CompanyRequestDTO.class)).thenReturn(companyDTO);

        CompanyResponseDTO result = companyService.updateCompany(1L, companyDTO);

        assertNotNull(result);
        assertEquals("Test Company", result.getName());
        assertEquals("Test Description", result.getDescription());

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepo).save(captor.capture());
        assertEquals("Test Company", captor.getValue().getName());
        assertEquals("Test Description", captor.getValue().getDescription());
    }

    @Test
    void testUpdateCompany_NotFound() {
        when(companyRepo.findById(1L)).thenReturn(Optional.empty());

        CompanyServiceException exception = assertThrows(
                CompanyServiceException.class,
                () -> companyService.updateCompany(1L, companyDTO)
        );

        assertEquals(ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testCreateCompany() {
        when(modelMapper.map(companyDTO, Company.class)).thenReturn(company);

        String result = companyService.createCompany(companyDTO);

        assertEquals("COMPANY CREATED SUCCESSFULLY", result);

        verify(companyHelper, times(1)).validateCompanyRequest(companyDTO);
        verify(companyRepo, times(1)).save(company);
    }

    @Test
    void testGetCompanyById_Success() {
        when(companyRepo.findById(1L)).thenReturn(Optional.of(company));
        when(modelMapper.map(company, CompanyRequestDTO.class)).thenReturn(companyDTO);

        CompanyRequestDTO result = companyService.getCompanyById(1L);

        assertNotNull(result);
        assertEquals("Test Company", result.getName());
        verify(companyRepo).findById(1L);
    }

    @Test
    void testGetCompanyById_NotFound() {
        when(companyRepo.findById(1L)).thenReturn(Optional.empty());

        CompanyServiceException exception = assertThrows(
                CompanyServiceException.class,
                () -> companyService.getCompanyById(1L)
        );

        assertEquals(ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testDeleteCompany_Success() {
        when(companyRepo.findById(1L)).thenReturn(Optional.of(company));

        String result = companyService.deleteCompany(1L);

        assertEquals("COMPANY DELETED SUCCESSFULLY", result);
        verify(companyRepo).delete(company);
    }

    @Test
    void testDeleteCompany_NotFound() {
        when(companyRepo.findById(1L)).thenReturn(Optional.empty());

        CompanyServiceException exception = assertThrows(
                CompanyServiceException.class,
                () -> companyService.deleteCompany(1L)
        );

        assertEquals(ErrorCodeEnum.COMPANY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void testUpdateCompanyRating() {
        ReviewMessage reviewMessage = new ReviewMessage();
        reviewMessage.setCompanyId(1L);
        reviewMessage.setId(100L);

        when(companyRepo.findById(1L)).thenReturn(Optional.of(company));
        when(reviewClient.getAverageRating(1L)).thenReturn(4.8);

        companyService.updateCompanyRating(reviewMessage);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepo).save(captor.capture());

        assertEquals(4.8, captor.getValue().getRating());
        verify(reviewClient).getAverageRating(1L);
    }
}
