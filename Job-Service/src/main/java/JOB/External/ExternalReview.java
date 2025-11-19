package JOB.External;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ExternalReview {
    private Long id;
    private String title;
    private String description;
    private double rating;

}
