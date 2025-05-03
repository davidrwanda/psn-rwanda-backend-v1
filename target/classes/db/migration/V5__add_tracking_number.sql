-- Add tracking_number column to bookings table
ALTER TABLE bookings ADD COLUMN tracking_number VARCHAR(20) UNIQUE;

-- Update existing bookings to have a tracking number
-- For each existing booking, update the tracking number with format PSN-001, PSN-002, etc.
CREATE OR REPLACE FUNCTION update_existing_booking_tracking_numbers()
RETURNS VOID AS $$
DECLARE
    booking_id BIGINT;
    counter INT := 1;
    track_number VARCHAR(20);
BEGIN
    FOR booking_id IN SELECT id FROM bookings ORDER BY id
    LOOP
        track_number := 'PSN-' || LPAD(counter::text, 3, '0');
        UPDATE bookings SET tracking_number = track_number WHERE id = booking_id;
        counter := counter + 1;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT update_existing_booking_tracking_numbers();

-- Drop the function after use
DROP FUNCTION update_existing_booking_tracking_numbers(); 