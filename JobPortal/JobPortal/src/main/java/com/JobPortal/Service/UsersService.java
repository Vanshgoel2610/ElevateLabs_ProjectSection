package com.JobPortal.Service;

import com.JobPortal.Entity.dto.UsersDto;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UsersService {
    UsersDto registerUser(UsersDto userDto);
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}