package com.psnrwanda.api.service;

import com.psnrwanda.api.dto.UserDto;
import com.psnrwanda.api.exception.ResourceAlreadyExistsException;
import com.psnrwanda.api.exception.ResourceNotFoundException;
import com.psnrwanda.api.model.User;
import com.psnrwanda.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for user management
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     *
     * @param registrationDto the registration information
     * @return registration response with user information
     */
    @Transactional
    public UserDto.RegistrationResponseDto registerUser(UserDto.RegistrationDto registrationDto) {
        log.info("Registering new user with username: {}", registrationDto.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new ResourceAlreadyExistsException("User", "username", registrationDto.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", registrationDto.getEmail());
        }
        
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFullName(registrationDto.getFullName());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setRole("ROLE_USER");
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());
        
        return new UserDto.RegistrationResponseDto(
                convertToDto(savedUser),
                "User registered successfully"
        );
    }
    
    /**
     * Get user by id
     *
     * @param id user id
     * @return user data
     */
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return convertToDto(user);
    }
    
    /**
     * Get user by username
     *
     * @param username username
     * @return user data
     */
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return convertToDto(user);
    }
    
    /**
     * Convert User entity to UserDto
     *
     * @param user user entity
     * @return user dto
     */
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
} 