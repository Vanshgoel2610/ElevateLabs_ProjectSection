package com.JobPortal.Service.impl;

import com.JobPortal.Entity.Applications;
import com.JobPortal.Entity.Jobs;
import com.JobPortal.Entity.UpdateStatusRequest;
import com.JobPortal.Entity.Users;
import com.JobPortal.Entity.dto.ApplicationDto;
import com.JobPortal.Entity.type.Status;
import com.JobPortal.Repository.ApplicationRepository;
import com.JobPortal.Repository.JobRepository;
import com.JobPortal.Repository.UsersRepository;
import com.JobPortal.Service.ApplicationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UsersRepository usersRepository;
    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;


    @Override
    public ApplicationDto applyToJob(Long jobId, Long applicantId) {
        Users applicant = usersRepository.findById(applicantId)
                .orElseThrow(() -> new EntityNotFoundException("Applicant not found with id: " + applicantId));

        Jobs job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + jobId));

        if (applicationRepository.existsByJob_IdAndApplicant_Id(jobId, applicantId)) {
            throw new IllegalStateException("You have already applied for this job.");
        }

        Applications newApplication = new Applications();
        newApplication.setApplicant(applicant);
        newApplication.setJob(job);
        newApplication.setStatus(Status.Submitted);

        Applications savedApplication = applicationRepository.save(newApplication);

        return modelMapper.map(savedApplication, ApplicationDto.class);
    }

    @Override
    public List<ApplicationDto> getApplicationsForJob(Long jobId, Long employerId) {
        Jobs job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + jobId));

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new AccessDeniedException("You are not authorized to view applications for this job.");
        }

        List<Applications> applications = applicationRepository.findByJob_Id(jobId);
        return applications.stream()
                .map(application -> modelMapper.map(application, ApplicationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationDto> getApplicationsForApplicant(Long applicantId) {
        if (!usersRepository.existsById(applicantId)) {
            throw new EntityNotFoundException("Applicant not found with id: " + applicantId);
        }

        List<Applications> applications = applicationRepository.findByApplicant_Id(applicantId);
        return applications.stream()
                .map(application -> modelMapper.map(application, ApplicationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDto updateApplicationStatus(Long applicationId, UpdateStatusRequest newStatus, Long employerId) {
        Applications application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with id: " + applicationId));

        if (!application.getJob().getEmployer().getId().equals(employerId)) {
            throw new AccessDeniedException("You are not authorized to update this application.");
        }

        application.setStatus(newStatus.getStatus());
        Applications updatedApplication = applicationRepository.save(application);
        return modelMapper.map(updatedApplication, ApplicationDto.class);
    }
}