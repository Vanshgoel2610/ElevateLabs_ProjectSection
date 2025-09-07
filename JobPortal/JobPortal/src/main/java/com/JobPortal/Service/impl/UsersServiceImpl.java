package com.JobPortal.Service.impl;

import com.JobPortal.Entity.Users;
import com.JobPortal.Entity.dto.UsersDto;
import com.JobPortal.Repository.UsersRepository;
import com.JobPortal.Service.UsersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UsersDto registerUser(UsersDto userDto) {
        // Check if username already exists
//        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
//            throw new IllegalStateException("Username already exists: " + userDto.getUsername());
//        }

        Users user = modelMapper.map(userDto, Users.class);
        user.setPassword(user.getPassword());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UsersDto.class);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Users user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//        return new User (
//                user.getUsername(),
//                user.getPassword(),
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
//        );
//    }
}