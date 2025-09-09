package com.JobPortal.Service.impl;

import com.JobPortal.Entity.Applications;
import com.JobPortal.Entity.Jobs;
import com.JobPortal.Entity.UpdateStatusRequest;
import com.JobPortal.Entity.Users;
import com.JobPortal.Entity.dto.ApplicationDto;
import com.JobPortal.Entity.dto.JobsDto;
import com.JobPortal.Entity.dto.ReferralRequestDto;
import com.JobPortal.Entity.dto.UsersDto;
import com.JobPortal.Entity.type.Role;
import com.JobPortal.Entity.type.Status;
import com.JobPortal.Repository.ApplicationRepository;
import com.JobPortal.Repository.JobRepository;
import com.JobPortal.Repository.UsersRepository;
import com.JobPortal.Security.JwtUtil;
import com.JobPortal.Service.ApplicationService;
import com.JobPortal.Service.EmailService;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UsersRepository usersRepository;
    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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

        return this.mapToApplicationDto(savedApplication);
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
                .map(this::mapToApplicationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationDto> getApplicationsForApplicant(Long applicantId) {
        if (!usersRepository.existsById(applicantId)) {
            throw new EntityNotFoundException("Applicant not found with id: " + applicantId);
        }

        List<Applications> applications = applicationRepository.findByApplicant_Id(applicantId);
        return applications.stream()
                .map(this::mapToApplicationDto)
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
        return mapToApplicationDto(updatedApplication);
    }

    private ApplicationDto mapToApplicationDto(Applications application) {
        ApplicationDto dto = new ApplicationDto();
        dto.setId(application.getId());
        dto.setStatus(application.getStatus());

        if (application.getJob() != null) {
            JobsDto jobDto = new JobsDto();
            jobDto.setId(application.getJob().getId());
            jobDto.setTitle(application.getJob().getTitle());
            jobDto.setDescription(application.getJob().getDescription());
            jobDto.setCompany_name(application.getJob().getCompany_name());
            if (application.getJob().getEmployer() != null) {
                UsersDto employerDto = new UsersDto();
                employerDto.setId(application.getJob().getEmployer().getId());
                employerDto.setUsername(application.getJob().getEmployer().getUsername());
                employerDto.setEmail(application.getJob().getEmployer().getEmail());
                jobDto.setEmployer(employerDto);
            }
            dto.setJob(jobDto);
        }

        if (application.getApplicant() != null) {
            UsersDto applicantDto = new UsersDto();
            applicantDto.setId(application.getApplicant().getId());
            applicantDto.setUsername(application.getApplicant().getUsername());
            applicantDto.setEmail(application.getApplicant().getEmail());
            dto.setApplicant(applicantDto);
        }

        return dto;
    }

    @Override
    public String createReferral(ReferralRequestDto referralRequest, Long employerId) {
        Jobs job = jobRepository.findById(referralRequest.getJobId())
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + referralRequest.getJobId()));

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new AccessDeniedException("You are not authorized to create referrals for this job.");
        }
        String token = "http://localhost:8080/api/applications/apply-from-referral?token=" + jwtUtil.generateReferralToken(referralRequest.getJobId(), referralRequest.getApplicantEmail());

        String subject = "You've been referred for a job: " + job.getTitle();
        String body = "You have been referred for the '" + job.getTitle() + "' position at "
                + job.getCompany_name() + ".\n\n"
                + "Click the link below to apply automatically:\n" + token;
        emailService.sendEmail(referralRequest.getApplicantEmail(), subject, body);

        return token;
    }

    @Override
    public ApplicationDto applyFromReferral(String token) {
        Claims claims = jwtUtil.getClaimsFromToken(token);
        Long jobId = Long.parseLong(claims.get("jobId", String.class));
        String applicantEmail = claims.getSubject();

        Users applicant = usersRepository.findByEmail(applicantEmail)
            .orElseGet(() -> {
                Users newUser = new Users();
                newUser.setUsername(applicantEmail);
                newUser.setPassword(passwordEncoder.encode("temporaryPassword123"));
                Set<Role> roles = new HashSet<>();
                roles.add(Role.Applicant);
                newUser.setRole(roles);
                return usersRepository.save(newUser);
            });

        return this.applyToJob(jobId, applicant.getId());
    }
}