package com.JobPortal.Controller;

import com.JobPortal.Entity.UpdateStatusRequest;
import com.JobPortal.Entity.Users;
import com.JobPortal.Entity.dto.ApplicationDto;
import com.JobPortal.Repository.UsersRepository;
import com.JobPortal.Service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final UsersRepository userRepository;

    @PostMapping("/apply/{jobId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApplicationDto> applyToJob(@PathVariable Long jobId, Authentication authentication) {
        Users currentUser = getCurrentUser(authentication);
        ApplicationDto newApplication = applicationService.applyToJob(jobId, currentUser.getId());
        return ResponseEntity.ok(newApplication);
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('Employer')")
    public ResponseEntity<List<ApplicationDto>> getApplicationsForJob(@PathVariable Long jobId, Authentication authentication) {
        Users currentUser = getCurrentUser(authentication);
        List<ApplicationDto> applications = applicationService.getApplicationsForJob(jobId, currentUser.getId());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/my-applications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ApplicationDto>> getMyApplications(Authentication authentication) {
        Users currentUser = getCurrentUser(authentication);
        List<ApplicationDto> applications = applicationService.getApplicationsForApplicant(currentUser.getId());
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('Employer')")
    public ResponseEntity<ApplicationDto> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateStatusRequest statusRequest,
            Authentication authentication
    ) {
        Users currentUser = getCurrentUser(authentication);
        ApplicationDto updatedApplication = applicationService.updateApplicationStatus(
                applicationId,
                statusRequest,
                currentUser.getId()
        );
        return ResponseEntity.ok(updatedApplication);
    }

    // TODO: A controller for giving referral using mail schedular

    private Users getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}