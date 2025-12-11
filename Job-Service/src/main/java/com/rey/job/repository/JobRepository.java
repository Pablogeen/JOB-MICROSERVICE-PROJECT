package com.rey.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rey.job.entity.Job;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
  Optional<List<Job>> findAllJobsByCompanyId(Long companyId);

    void deleteAllByCompanyId(Long companyId);

}
