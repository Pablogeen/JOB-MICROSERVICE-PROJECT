package JOB.Client;

import JOB.External.ExternalReview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name ="REVIEW-SERVICE",
        url="${review-service.url}")
public interface ReviewClient {

    @GetMapping("/reviews/{companyId}")
    List<ExternalReview> getReviews(@RequestParam("companyId") Long companyId);

}
