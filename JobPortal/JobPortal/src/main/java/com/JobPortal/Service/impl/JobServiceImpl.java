package com.JobPortal.Service.impl;

import com.JobPortal.Entity.Jobs;
import com.JobPortal.Entity.Users;
import com.JobPortal.Entity.dto.JobsDto;
import com.JobPortal.Entity.type.Role;
import com.JobPortal.Repository.JobRepository;
import com.JobPortal.Repository.UsersRepository;
import com.JobPortal.Service.JobService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public JobsDto createJob(JobsDto jobDto, Long employerId) {
        Users employer = userRepository.findById(employerId)
                .orElseThrow(() -> new EntityNotFoundException("Employer not found with ID: " + employerId));

//        if (employer.getRole() != Role.Employer) {
//            throw new AccessDeniedException("Only users with the EMPLOYER role can post jobs.");
//        }

        Jobs job = modelMapper.map(jobDto, Jobs.class);
        job.setEmployer(employer);

        Jobs savedJob = jobRepository.save(job);
        return modelMapper.map(savedJob, JobsDto.class);
    }

    @Override
    public List<JobsDto> getAllJobs() {
        List<Jobs> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(job -> modelMapper.map(job, JobsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public JobsDto getJobById(Long jobId) {
        Jobs job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with ID: " + jobId));
        return modelMapper.map(job, JobsDto.class);
    }

    @Override
    public JobsDto updateJob(Long jobId, JobsDto jobDto, Long employerId) {
        Jobs existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with ID: " + jobId));

//        if (!existingJob.getEmployer().getId().equals(employerId)) {
//            throw new AccessDeniedException("You are not authorized to update this job.");
//        }

        // Update fields
        existingJob.setTitle(jobDto.getTitle());
        existingJob.setDescription(jobDto.getDescription());
        existingJob.setCompany_name(jobDto.getCompany_name());

        Jobs updatedJob = jobRepository.save(existingJob);
        return modelMapper.map(updatedJob, JobsDto.class);
    }

    @Override
    public void deleteJob(Long jobId, Long employerId) {
        Jobs job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with ID: " + jobId));

//        if (!job.getEmployer().getId().equals(employerId)) {
//            throw new AccessDeniedException("You are not authorized to delete this job.");
//        }

        jobRepository.delete(job);
    }
}