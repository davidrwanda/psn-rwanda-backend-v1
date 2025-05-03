-- Create booking_documents table for storing document information
CREATE TABLE IF NOT EXISTS booking_documents (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);

-- Create index on booking_id for better performance
CREATE INDEX idx_booking_documents_booking_id ON booking_documents(booking_id);

-- Update bookings table to make email and full_name fields nullable (if needed)
ALTER TABLE bookings ALTER COLUMN email DROP NOT NULL;
ALTER TABLE bookings ALTER COLUMN full_name DROP NOT NULL; 