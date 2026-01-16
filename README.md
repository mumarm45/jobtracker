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

## Getting Started

### 1. Configure Database

**Option A: Using environment variables (Recommended)**

```bash
export MONGODB_URI="mongodb://your-username:your-password@localhost:27017/jobtracker?authSource=admin"
gradle bootRun
```

**Option B: Using application-{profile}.yml**

Create an environment-specific configuration file:

```bash
cp src/main/resources/application.yml src/main/resources/application-dev.yml
# Edit application-dev.yml with your credentials
# This file is gitignored
```

**Note:** The `.env` file is provided as a template for storing credentials securely. While Spring Boot doesn't natively load `.env` files, you can source it before running:

```bash
export $(cat .env | xargs) && gradle bootRun
```

### 2. Run the Application

```bash
gradle bootRun
```

The API will start on `http://localhost:8080`

### Run Tests

```bash
gradle test
```



### Queries

- `GET /api/jobs/status/{status}` - Get jobs by status
- `GET /api/jobs/search?keyword=engineer` - Search jobs
- `GET /api/jobs/priority/{priority}` - Get jobs by priority

## Docker Setup

### Option 1: Run Everything with Docker Compose (Recommended)

Build and start both MongoDB and the application:

```bash
docker-compose up --build
```

To run in detached mode:

```bash
docker-compose up --build -d
```

Stop the services:

```bash
docker-compose down
```

### Option 2: Run Only MongoDB in Docker

Start only MongoDB:

```bash
docker-compose up mongodb -d
```

Then run the application locally (see "Getting Started" section above).

### Docker Image Management

Build the application image separately:

```bash
docker build -t jobtracker-app .
```

Run the container:

```bash
docker run -p 8080:8080 \
  -e MONGODB_URI=mongodb://admin:password@host.docker.internal:27017/jobtracker?authSource=admin \
  jobtracker-app
```

View logs:

```bash
docker-compose logs -f app
```

### Docker Features

The Dockerfile includes:
- **Multi-stage build**: Smaller final image size (~200MB vs ~800MB)
- **Security**: Runs as non-root user
- **Health checks**: Automatic container health monitoring
- **Dependency caching**: Faster rebuilds when only code changes
- **Production-ready**: Uses Eclipse Temurin JRE for reliability

## Security Notes

**Important:** Never commit sensitive credentials to Git!

- `.env` files are automatically loaded by Spring Boot 4.0+
- The `.env` file is gitignored and will not be committed
- An example is provided in `.env.example`
- The `application.yml` file uses environment variables for database credentials
- Profile-specific configs (`application-*.yml`) are also gitignored
- Always use environment variables or a secrets manager for production

### Environment Variables

The following environment variables can be set in your `.env` file:

| Variable | Description | Default |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB connection string | `mongodb://localhost:27017/jobtracker` |
| `SERVER_PORT` | Server port | `8080` |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins | `http://localhost:3000,http://localhost:4200` |
