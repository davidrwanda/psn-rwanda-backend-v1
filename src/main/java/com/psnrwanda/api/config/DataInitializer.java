package com.psnrwanda.api.config;

import com.psnrwanda.api.model.User;
import com.psnrwanda.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Initialize application data on startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Run data initialization on application startup
     */
    @Override
    @Transactional
    public void run(String... args) {
        createAdminUserIfNotExists();
    }

    /**
     * Create admin user if not exists
     */
    private void createAdminUserIfNotExists() {
        if (!userRepository.existsByUsername("admin")) {
            log.info("Creating default admin user");
            User adminUser = User.builder()
                    .username("admin")
                    .email("admin@psnrwanda.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Administrator")
                    .phoneNumber("+250788000000")
                    .role("ROLE_ADMIN")
                    .enabled(true)
                    .build();
            
            userRepository.save(adminUser);
            log.info("Default admin user created successfully");
        } else {
            log.info("Admin user already exists, skipping creation");
        }
    }
} 