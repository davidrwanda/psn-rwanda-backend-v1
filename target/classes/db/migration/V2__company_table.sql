-- Company table
CREATE TABLE company (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(100),
    company_code VARCHAR(50),
    incorporated_on DATE,
    website VARCHAR(255),
    logo_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Insert default company information
INSERT INTO company (name, description, address, phone, email, company_code, incorporated_on, created_at)
VALUES (
    'PSN RWANDA Ltd',
    'Legal Activities (Notary Services), Consultancy, Leasing, Real Estate, R&D',
    'Nyamabuye, Muhanga, Southern Province, Rwanda',
    '+250 788 859 612',
    'info@psnrwanda.com',
    '121058604',
    '2023-01-23',
    NOW()
); 