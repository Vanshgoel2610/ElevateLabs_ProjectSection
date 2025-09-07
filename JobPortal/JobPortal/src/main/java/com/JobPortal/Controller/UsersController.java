package com.JobPortal.Controller;

import com.JobPortal.Entity.Users;
import com.JobPortal.Entity.dto.UsersDto;
import com.JobPortal.Repository.UsersRepository;
import com.JobPortal.Service.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService userService;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UsersDto userDto) {
        try {
            UsersDto registeredUser = userService.registerUser(userDto);
            // We should not return the password in the response
//            registeredUser.setPassword(null);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{userId}/me")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsersDto> getCurrentUserProfile(@PathVariable Long userId) {
        Users currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
//        Users currentUser = getCurrentUser(authentication);
        UsersDto userDto = modelMapper.map(currentUser, UsersDto.class);
        // Ensure password is not sent in the response
        userDto.setPassword(null);
        return ResponseEntity.ok(userDto);
    }


//    private Users getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//    }
}