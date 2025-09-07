package com.JobPortal.Service;

import com.JobPortal.Entity.UpdateStatusRequest;
import com.JobPortal.Entity.dto.ApplicationDto;
import com.JobPortal.Entity.type.Status;

import java.util.List;

public interface ApplicationService {
    ApplicationDto applyToJob(Long jobId, Long applicantId);
    List<ApplicationDto> getApplicationsForJob(Long jobId, Long employerId);
    List<ApplicationDto> getApplicationsForApplicant(Long applicantId);
    ApplicationDto updateApplicationStatus(Long applicationId, UpdateStatusRequest newStatus, Long employerId);
}