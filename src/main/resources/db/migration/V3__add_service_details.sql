-- Add new columns to services table
ALTER TABLE services
ADD COLUMN turnaround_time VARCHAR(100),
ADD COLUMN cta_text VARCHAR(100),
ADD COLUMN price_info VARCHAR(100),
ADD COLUMN additional_info TEXT;

-- Create service_bullet_points table
CREATE TABLE service_bullet_points (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    bullet_point TEXT,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

-- Create index on service_id for better performance
CREATE INDEX idx_service_bullet_points_service_id ON service_bullet_points(service_id); 