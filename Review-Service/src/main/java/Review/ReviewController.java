package Review;

import Messaging.ReviewMessageProducer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {

     private ReviewService service;
     private ReviewMessageProducer messageProducer;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam("companyId") Long companyId){
        return new ResponseEntity<>(service.getAllReviews(companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addReview(@RequestParam("companyId") Long companyId, @RequestBody Review review){
        var request = service.addReview(companyId, review);
        messageProducer.sendMessage(review);
        return new ResponseEntity<>(request, HttpStatus.CREATED);

    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable("reviewId") Long reviewId){
        return new ResponseEntity<>(service.getReviewById(reviewId), HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody Review review){
        return new ResponseEntity<>(service.updateReview( reviewId, review), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long reviewId){
        return new ResponseEntity<>(service.deleteReview(reviewId), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/averageRating")
    public ResponseEntity<Double> getAverageRating(@RequestParam Long companyId){
        return new ResponseEntity<>(service.getAverageRating(companyId), HttpStatus.OK);
    }


}
