package com.JobPortal.Repository;

import com.JobPortal.Entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Applications, Long> {
    Boolean existsByJob_IdAndApplicant_Id(Long jobId, Long applicationId);
    List<Applications> findByJob_Id(Long id);
    List<Applications> findByApplicant_Id(Long application_id);
}