package com.JobPortal.Controller;

import com.JobPortal.Entity.dto.LoginRequestDto;
import com.JobPortal.Entity.dto.LoginResponseDto;
import com.JobPortal.Entity.dto.SignupRequestDto;
import com.JobPortal.Entity.dto.SignupResponseDto;
import com.JobPortal.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<SignupResponseDto> register(@RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.registerEmployer(signupRequestDto));
    }
}