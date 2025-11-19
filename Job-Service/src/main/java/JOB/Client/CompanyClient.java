package JOB.Client;

import JOB.External.ExternalCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "COMPANY-SERVICE",
        url="${company-service.url}")
public interface CompanyClient {

    @GetMapping("/company/{id}")
   ExternalCompany getCompany(@PathVariable("id") Long id);

}
