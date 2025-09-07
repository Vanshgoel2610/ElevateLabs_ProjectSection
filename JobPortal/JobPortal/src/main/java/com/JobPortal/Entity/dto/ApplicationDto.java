package com.JobPortal.Entity.dto;

import com.JobPortal.Entity.type.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDto {
    private Long id;
    private UsersDto applicant;
    private JobsDto job;
    @Enumerated(EnumType.STRING)
    private Status status;
}