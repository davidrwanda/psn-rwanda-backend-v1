package com.psnrwanda.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple controller for testing public endpoints
 */
@RestController
@RequestMapping("/api/v1/test")
@Slf4j
public class TestController {

    /**
     * Test endpoint that should be publicly accessible
     * @return simple text response
     */
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        log.info("Public test endpoint accessed");
        return ResponseEntity.ok("This is a public endpoint");
    }
} 