-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100),
    phone_number VARCHAR(20),
    enabled BOOLEAN DEFAULT TRUE,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Services table
CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    image_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Bookings table
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(20) NOT NULL,
    service_id BIGINT REFERENCES services(id),
    user_id BIGINT REFERENCES users(id),
    email VARCHAR(100),
    full_name VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, email, password, full_name, role, created_at)
VALUES ('admin', 'admin@psnrwanda.com', '$2a$10$fJ4GBjcQsfKjFAIzm7MZzOGhTftlV5jcrzuKcSiQHAkONxAp4.zmi', 'Administrator', 'ROLE_ADMIN', NOW());

-- Insert default services
INSERT INTO services (title, description, is_active, created_at)
VALUES 
    ('Notary Services', 'Professional notary services for document authentication and legal validation', TRUE, NOW()),
    ('Legal Consultancy', 'Expert legal advice and consultancy for individuals and businesses', TRUE, NOW()),
    ('Real Estate Services', 'Professional real estate brokerage and advisory services', TRUE, NOW()),
    ('Accounting Services', 'Financial accounting and bookkeeping services for businesses', TRUE, NOW()),
    ('Leasing Services', 'Professional leasing and property management services', TRUE, NOW()); 