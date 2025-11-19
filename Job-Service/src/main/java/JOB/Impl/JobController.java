package JOB.Impl;

import JOB.DTO.JobDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/job")
@AllArgsConstructor
public class JobController {

    private JobService service;

    @GetMapping
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        return new ResponseEntity<>(service.getAllJobs(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postJob(@RequestBody Job job) {
        return new ResponseEntity<>(service.createJob(job), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
     return new ResponseEntity<>(service.deleteJobById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
        public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job job){
          return new ResponseEntity<>(service.updateJob(id, job), HttpStatus.NOT_FOUND);
        }
}
