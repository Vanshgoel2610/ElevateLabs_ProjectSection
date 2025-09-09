package com.JobPortal.Service.impl;

import com.JobPortal.Entity.Users;
import com.JobPortal.Entity.dto.LoginRequestDto;
import com.JobPortal.Entity.dto.LoginResponseDto;
import com.JobPortal.Entity.dto.SignupRequestDto;
import com.JobPortal.Entity.dto.SignupResponseDto;
import com.JobPortal.Entity.type.Role;
import com.JobPortal.Repository.UsersRepository;
import com.JobPortal.Security.JwtUtil;
import com.JobPortal.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate (
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        Users users = (Users)authentication.getPrincipal();
        String token = jwtUtil.generateAccessToken(users);
        return new LoginResponseDto(token, users.getId());
    }

    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        if(usersRepository.findByUsername(signupRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(Role.Applicant);

        Users newUser = Users.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role(roles)
                .email(signupRequestDto.getEmail())
                .build();
        usersRepository.save(newUser);

        return new SignupResponseDto(newUser.getId(), newUser.getUsername(), newUser.getRole().toString());
    }

    public SignupResponseDto registerEmployer(SignupRequestDto signupRequestDto) {
        if(usersRepository.findByUsername(signupRequestDto.getUsername()).isPresent()) throw new IllegalArgumentException("User already exists");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.Employer);

        Users newUser = Users.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role(roles)
                .email(signupRequestDto.getEmail())
                .build();
        usersRepository.save(newUser);

        return new SignupResponseDto(newUser.getId(), newUser.getUsername(), newUser.getRole().toString());
    }
}