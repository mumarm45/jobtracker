# Job Tracker API

A reactive REST API for tracking job applications built with Spring Boot WebFlux and MongoDB.

## Tech Stack

- Java 19
- Spring Boot 4.0.1
- Spring WebFlux (Reactive)
- MongoDB (Reactive)
- Gradle

## Prerequisites

- Java 19
- MongoDB running on `localhost:27017`
- MongoDB credentials: `<user>/password`

## Getting Started

### Run the Application

```bash
gradle bootRun
```

The API will start on `http://localhost:8080`

### Run Tests

```bash
gradle test
```

## API Endpoints

### Jobs

- `GET /api/jobs?offset=0&limit=10` - Get paginated jobs
- `GET /api/jobs/{id}` - Get job by ID
- `POST /api/jobs` - Create new job
- `PUT /api/jobs/{id}` - Update job
- `DELETE /api/jobs/{id}` - Delete job

### Job Management

- `PATCH /api/jobs/{id}/status?status=APPLIED` - Update job status
- `POST /api/jobs/{id}/interviews` - Add interview

### Queries

- `GET /api/jobs/status/{status}` - Get jobs by status
- `GET /api/jobs/search?keyword=engineer` - Search jobs
- `GET /api/jobs/priority/{priority}` - Get jobs by priority

### Statistics

- `GET /api/jobs/stats/total` - Get total job count
- `GET /api/jobs/stats/status/{status}` - Get count by status


- `data`: Array of job objects
- `total`: Total number of jobs in database
- `next`: Offset for next page (null if no more items)
- `offset`: Current offset
- `limit`: Current page size

## Job Statuses

- `WISHLIST` - Job saved for future
- `APPLIED` - Application submitted
- `INTERVIEWING` - In interview process
- `OFFERED` - Received offer
- `REJECTED` - Application rejected
- `ACCEPTED` - Offer accepted
- `WITHDRAWN` - Application withdrawn

## Database Configuration

MongoDB configuration is in `src/main/resources/application-dev.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://<user>:<password>@localhost:27017/jobTracker?authSource=admin
```

## Docker Setup
I ran monogodb in docker. 
Start MongoDB with Docker:

```bash
docker-compose up -d
```
