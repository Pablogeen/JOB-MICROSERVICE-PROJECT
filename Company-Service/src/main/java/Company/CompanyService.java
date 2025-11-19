package Company;

import Clients.ReviewClient;
import DTO.ReviewMessage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {
    private CompanyRepository repo;
    private ReviewClient reviewClient;

    public List<Company> getAllCompanies() {
        return repo.findAll();
    }

    public String updateCompany(Long id, Company company) {

        Company existingCompany = repo.findById(id)
                .orElseThrow(()-> new IllegalStateException("COMPANY NOT FOUND"));

        existingCompany.setName(company.getName());
        existingCompany.setDescription(company.getDescription());

        repo.save(existingCompany);
        return "COMPANY UPDATED SUCCESSFULLY";
    }

    public String createCompany(Company company) {
        repo.save(company);
        return "COMPANY CREATED SUCCESSFULLY";
    }

    public Company getCompanyById(Long id) {
        return repo.findById(id)
                .orElseThrow(()-> new IllegalStateException("COMPANY NOT FOUND"));
    }

    public String deleteCompany(Long id) {
        Company existingCompany = repo.findById(id)
                .orElseThrow(()-> new IllegalStateException("COMPANY NOT FOUND"));

        repo.delete(existingCompany);
        return  "COMPANY DELETED SUCCESSFULLY";
    }

    public void updateCompanyRating(ReviewMessage reviewMessage){
            Company company = repo.findById(reviewMessage.getCompanyId())
                    .orElseThrow(()->  new IllegalStateException("REVIEW NOT FOUND"));

            double averageRating = reviewClient.getAverageRating(reviewMessage.getCompanyId());
            company.setRating(averageRating);
            repo.save(company);
    }
}
