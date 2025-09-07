package com.JobPortal.Entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobsDto {
    private Long id;
    private String title;
    private String description;
    private String company_name;
    private UsersDto employer;
}