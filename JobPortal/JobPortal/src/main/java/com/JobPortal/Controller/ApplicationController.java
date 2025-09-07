package com.JobPortal.Controller;

import com.JobPortal.Entity.UpdateStatusRequest;
import com.JobPortal.Entity.Users;
import com.JobPortal.Entity.dto.ApplicationDto;
import com.JobPortal.Entity.type.Status;
import com.JobPortal.Repository.UsersRepository;
import com.JobPortal.Service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final UsersRepository userRepository; // Needed to get the current user's ID

    @PostMapping("/apply/{jobId}/{userId}")
//    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<ApplicationDto> applyToJob(@PathVariable Long jobId, @PathVariable Long userId) {
        Users currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
//        Users currentUser = getCurrentUser(authentication);
        ApplicationDto newApplication = applicationService.applyToJob(jobId, currentUser.getId());
        return ResponseEntity.ok(newApplication);
    }

    @GetMapping("/job/{jobId}/{userId}")
//    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<ApplicationDto>> getApplicationsForJob(@PathVariable Long jobId, @PathVariable Long userId) {
        Users currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
//        Users currentUser = getCurrentUser(authentication);
        List<ApplicationDto> applications = applicationService.getApplicationsForJob(jobId, currentUser.getId());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("{userId}/my-applications")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ApplicationDto>> getMyApplications(@PathVariable Long userId) {
        Users currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
//        Users currentUser = getCurrentUser(authentication);
        List<ApplicationDto> applications = applicationService.getApplicationsForApplicant(currentUser.getId());
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/{applicationId}/{userId}/status")
//    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApplicationDto> updateApplicationStatus(
            @PathVariable Long applicationId,
            @PathVariable Long userId,
            @RequestBody UpdateStatusRequest statusRequest
//            ,Authentication authentication
    ) {
        Users currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
//        Users currentUser = getCurrentUser(authentication);
        ApplicationDto updatedApplication = applicationService.updateApplicationStatus(
                applicationId,
                statusRequest,
                currentUser.getId()
        );
        return ResponseEntity.ok(updatedApplication);
    }

    // TODO: A controller for giving referral using mail schedular

//    private Users getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//    }
}