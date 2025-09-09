package com.JobPortal.Service;

import com.JobPortal.Entity.dto.JobsDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface JobService {
    JobsDto createJob(JobsDto job, Long employerId) throws AccessDeniedException;
    List<JobsDto> getAllJobs();
    JobsDto getJobById(Long jobId);
    JobsDto updateJob(Long jobId, JobsDto jobDto, Long employerId) throws AccessDeniedException;
    void deleteJob(Long jobId, Long employerId) throws AccessDeniedException;
}