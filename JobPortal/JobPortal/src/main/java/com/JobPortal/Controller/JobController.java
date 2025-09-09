package com.JobPortal.Controller;

import com.JobPortal.Entity.Users;
import com.JobPortal.Entity.dto.JobsDto;
import com.JobPortal.Repository.UsersRepository;
import com.JobPortal.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UsersRepository userRepository;

    @PostMapping()
    @PreAuthorize("hasRole('Employer')")
    public ResponseEntity<JobsDto> createJob(@RequestBody JobsDto jobDto, Authentication authentication) throws Exception {
        Users currentUser = getCurrentUser(authentication);
        JobsDto createdJob = jobService.createJob(jobDto, currentUser.getId());
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobsDto>> getAllJobs() {
        List<JobsDto> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobsDto> getJobById(@PathVariable Long id) {
        JobsDto job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Employer')")
    public ResponseEntity<JobsDto> updateJob(@PathVariable Long id, @RequestBody JobsDto jobDto, Authentication authentication) throws Exception {
        Users currentUser = getCurrentUser(authentication);
        JobsDto updatedJob = jobService.updateJob(id, jobDto, currentUser.getId());
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Employer')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id, Authentication authentication) throws AccessDeniedException {
        Users currentUser = getCurrentUser(authentication);
        jobService.deleteJob(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    private Users getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}