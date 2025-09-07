package com.JobPortal.Repository;


import com.JobPortal.Entity.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Jobs, Long> {
}