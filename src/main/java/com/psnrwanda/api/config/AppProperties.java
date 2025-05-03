package com.psnrwanda.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Application properties for type-safe configuration
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private final Cors cors = new Cors();
    private final Sms sms = new Sms();
    private final Company company = new Company();
    private final File file = new File();
    
    /**
     * CORS configuration properties
     */
    @Data
    public static class Cors {
        private String allowedOrigins;
        private String allowedMethods;
        private String allowedHeaders;
        private String exposedHeaders;
    }
    
    /**
     * SMS configuration properties
     */
    @Data
    public static class Sms {
        private boolean enabled;
        private String apiKey;
        private String apiUrl;
    }
    
    /**
     * Company information properties
     */
    @Data
    public static class Company {
        private String name;
        private String code;
        private String incorporatedOn;
        private String phone;
        private String email;
        private String address;
        private String description;
    }
    
    /**
     * File storage configuration properties
     */
    @Data
    public static class File {
        private String uploadDir;
    }
} 