package com.JobPortal.Service;

import com.JobPortal.Entity.dto.LoginRequestDto;
import com.JobPortal.Entity.dto.LoginResponseDto;
import com.JobPortal.Entity.dto.SignupRequestDto;
import com.JobPortal.Entity.dto.SignupResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    SignupResponseDto signup(SignupRequestDto signupRequestDto);
    SignupResponseDto registerEmployer(SignupRequestDto signupRequestDto);
}