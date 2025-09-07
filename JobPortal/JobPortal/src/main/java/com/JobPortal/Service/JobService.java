package com.JobPortal.Service;

import com.JobPortal.Entity.dto.JobsDto;

import java.util.List;

public interface JobService {
    JobsDto createJob(JobsDto jobDto, Long employerId);
    List<JobsDto> getAllJobs();
    JobsDto getJobById(Long jobId);
    JobsDto updateJob(Long jobId, JobsDto jobDto, Long employerId);
    void deleteJob(Long jobId, Long employerId);
}