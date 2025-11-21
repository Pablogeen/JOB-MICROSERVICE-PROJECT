package com.rey.job.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rey.job.Entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
