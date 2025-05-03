package com.psnrwanda.api.controller;

import com.psnrwanda.api.dto.UserDto;
import com.psnrwanda.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user management
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Endpoints for user management")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Register a new user
     *
     * @param registrationDto registration data
     * @return registration response
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Register a new user to enable tracking of service requests")
    public ResponseEntity<UserDto.RegistrationResponseDto> registerUser(
            @Valid @RequestBody UserDto.RegistrationDto registrationDto) {
        log.info("Received registration request for username: {}", registrationDto.getUsername());
        UserDto.RegistrationResponseDto response = userService.registerUser(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get user profile by ID
     *
     * @param id user ID
     * @return user data
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Get user details by ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Get user profile by username
     *
     * @param username username
     * @return user data
     */
    @GetMapping("/byUsername/{username}")
    @Operation(summary = "Get user by username", description = "Get user details by username")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
} 