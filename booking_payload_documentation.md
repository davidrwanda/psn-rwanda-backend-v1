# PSN Rwanda API - Booking Payload Documentation

## Booking Process with Document Uploads

The booking process now supports document uploads and has been simplified to require fewer mandatory fields. Here's how it works:

### Step 1: Upload Documents (Optional)

First, upload any documents that need to be attached to the booking:

**Endpoint:** `POST /api/v1/bookings/documents/upload`  
**Content-Type:** `multipart/form-data`

**Request:**
```
files: [file1.pdf, file2.docx, ...] (Multiple files can be uploaded)
```

**Response:**
```json
[
  {
    "id": 1,
    "fileName": "contract.pdf",
    "filePath": "9a7b8c6d-5e4f-3g2h-1i0j-k9l8m7n6o5p4.pdf",
    "fileType": "application/pdf",
    "fileSize": 1024000
  },
  {
    "id": 2,
    "fileName": "identity.jpg",
    "filePath": "1a2b3c4d-5e6f-7g8h-9i0j-k1l2m3n4o5p6.jpg",
    "fileType": "image/jpeg",
    "fileSize": 250000
  }
]
```

Take note of the document IDs returned in the response. These will be used in the booking request.

### Step 2: Create Booking

After uploading documents, create a booking using the document IDs:

**Endpoint:** `POST /api/v1/bookings/public`  
**Content-Type:** `application/json`

#### Full Booking Payload

```json
{
  "phoneNumber": "+250788123456",
  "serviceId": 1,
  "email": "client@example.com",
  "fullName": "John Doe",
  "notes": "Need urgent notary services",
  "documentIds": ["1", "2"]
}
```

#### Minimal Booking Payload

Only `phoneNumber` and `serviceId` are mandatory:

```json
{
  "phoneNumber": "+250788123456",
  "serviceId": 1
}
```

### Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| phoneNumber | String | Yes | Client's phone number (must be 10-15 digits) |
| serviceId | Number | Yes | ID of the service being booked |
| email | String | No | Client's email address |
| fullName | String | No | Client's full name |
| notes | String | No | Additional notes or requirements |
| documentIds | Array of Strings | No | IDs of previously uploaded documents |

### Response

```json
{
  "booking": {
    "id": 1,
    "trackingNumber": "PSN-001",
    "phoneNumber": "+250788123456",
    "serviceId": 1,
    "serviceName": "Notary Services",
    "email": "client@example.com",
    "fullName": "John Doe",
    "status": "PENDING",
    "notes": "Need urgent notary services",
    "createdAt": "2023-06-15 10:00:00",
    "updatedAt": "2023-06-15 10:00:00",
    "documents": [
      {
        "id": 1,
        "fileName": "contract.pdf",
        "filePath": "9a7b8c6d-5e4f-3g2h-1i0j-k9l8m7n6o5p4.pdf",
        "fileType": "application/pdf",
        "fileSize": 1024000
      },
      {
        "id": 2,
        "fileName": "identity.jpg",
        "filePath": "1a2b3c4d-5e6f-7g8h-9i0j-k1l2m3n4o5p6.jpg",
        "fileType": "image/jpeg",
        "fileSize": 250000
      }
    ]
  },
  "message": "Thank you! Your booking has been submitted successfully. Your tracking number is PSN-001. We will contact you shortly."
}
```

## Tracking Bookings

### Track by Phone Number

**Endpoint:** `POST /api/v1/bookings/track`  
**Content-Type:** `application/json`

**Request:**
```json
{
  "phoneNumber": "+250788123456"
}
```

**Response:**
```json
[
  {
    "id": 1,
    "trackingNumber": "PSN-001",
    "phoneNumber": "+250788123456",
    "serviceId": 1,
    "serviceName": "Notary Services",
    "status": "PENDING",
    "createdAt": "2023-06-15 10:00:00",
    "documents": [
      {
        "id": 1,
        "fileName": "contract.pdf"
      },
      {
        "id": 2,
        "fileName": "identity.jpg"
      }
    ]
  }
]
```

### Track by Tracking Number

**Endpoint:** `GET /api/v1/bookings/track/number/{trackingNumber}`  
**Content-Type:** `application/json`

**Request:**
GET /api/v1/bookings/track/number/PSN-001

**Response:**
```json
{
  "id": 1,
  "trackingNumber": "PSN-001",
  "phoneNumber": "+250788123456",
  "serviceId": 1,
  "serviceName": "Notary Services",
  "status": "PENDING",
  "createdAt": "2023-06-15 10:00:00",
  "documents": [
    {
      "id": 1,
      "fileName": "contract.pdf"
    },
    {
      "id": 2,
      "fileName": "identity.jpg"
    }
  ]
}
```

## Important Notes

1. **Document Upload**: Files are uploaded separately from the booking creation process
2. **Optional Fields**: Only phoneNumber and serviceId are mandatory; email and fullName are optional
3. **Document IDs**: After uploading documents, use the returned document IDs in the booking request
4. **File Size Limits**: Maximum file size is 10MB per file, with a total request size limit of 15MB
5. **Tracking Number**: Each booking is assigned a unique tracking number in format PSN-XXX (e.g. PSN-001) 