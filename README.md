# Aether Music Backend

Aether Music is a lossless music streaming application backend built with Spring Boot. It provides a robust API for managing users, tracks, albums, artists, playlists, and more, with features like user authentication, real-time play events, and Redis caching.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)
- [Frontend](#frontend)

## Features

- **User Management**: Registration, authentication, and user profiles.
- **Anonymous Playback**: Users can play songs without needing to log in, with optional account creation for personalized features like playlists and reactions.
- **Music Library**: Manage tracks, albums, artists, and genres.
- **Playlists**: Create, edit, and manage user playlists.
- **Streaming**: Handle track streaming with play events.
- **Reactions**: User reactions to tracks (like/dislike).
- **Real-time Events**: Redis-based event handling for play events.
- **Security**: JWT-based authentication and CORS configuration.
- **Caching**: Redis integration for performance.
- **Health Checks**: Redis health monitoring.

## Tech Stack

- **Backend**: Spring Boot 3.5.9
- **Language**: Java 21
- **Database**: PostgreSQL
- **Cache**: Redis
- **Authentication**: JWT (JSON Web Tokens)
- **Build Tool**: Maven
- **Other**: Lombok, Jackson, Spring Security, Spring Data JPA

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+
- Git

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/devendrahere/Aether.git
   cd Aether
   ```

2. **Install dependencies**:
   ```bash
   mvn clean install
   ```

3. **Set up the database**:
   - Install PostgreSQL and create a database named `aether`.
   - Update the database credentials in `src/main/resources/application.yml`.

4. **Set up Redis**:
   - Install Redis and ensure it's running on `localhost:6379`.

## Configuration

The application uses `application.yml` for configuration. Key settings include:

- **Database**:
  ```yaml
  spring:
    datasource:
      url: jdbc:postgresql://localhost:5432/aether
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
  ```

- **Redis**:
  ```yaml
  spring:
    data:
      redis:
        host: localhost
        port: 6379
  ```

- **JWT**:
  ```yaml
  security:
    jwt:
      secret: ${JWT_SECRET}
      expirationMills: 86400000
  ```

- **Music Storage**:
  ```yaml
  music:
    storage:
      base-path: /path/to/music/files
  ```

Set environment variables for sensitive data like `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET`.

## Database Schema

The application uses PostgreSQL with JPA for ORM. Below are the basic SQL CREATE TABLE statements for the main entities. Note that JPA handles table creation with `ddl-auto: update`, but these scripts can be used for manual setup or reference.

```sql
-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    profile_image_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

-- Genres table
CREATE TABLE genres (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Artists table
CREATE TABLE artists (
    id BIGSERIAL PRIMARY KEY,
    discogs_id BIGINT UNIQUE,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Artist-Genre relationship
CREATE TABLE artist_genres (
    artist_id BIGINT NOT NULL REFERENCES artists(id),
    genre_id BIGINT NOT NULL REFERENCES genres(id),
    PRIMARY KEY (artist_id, genre_id)
);

-- Albums table
CREATE TABLE albums (
    id BIGSERIAL PRIMARY KEY,
    discogs_id BIGINT UNIQUE,
    artist_id BIGINT NOT NULL REFERENCES artists(id),
    title VARCHAR(255) NOT NULL,
    release_year INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Tracks table
CREATE TABLE tracks (
    id BIGSERIAL PRIMARY KEY,
    discogs_id BIGINT,
    album_id BIGINT REFERENCES albums(id),
    title VARCHAR(255) NOT NULL,
    duration_sec INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Track-Artist relationship (many-to-many)
CREATE TABLE track_artists (
    track_id BIGINT NOT NULL REFERENCES tracks(id),
    artist_id BIGINT NOT NULL REFERENCES artists(id),
    PRIMARY KEY (track_id, artist_id)
);

-- Track Files table
CREATE TABLE track_files (
    id BIGSERIAL PRIMARY KEY,
    track_id BIGINT NOT NULL REFERENCES tracks(id),
    quality VARCHAR(20) NOT NULL,
    codec VARCHAR(20) NOT NULL,
    file_path TEXT NOT NULL,
    file_size_bytes BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (track_id, quality)
);

-- Playlists table
CREATE TABLE playlists (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

-- Playlist-Tracks relationship
CREATE TABLE playlist_tracks (
    playlist_id BIGINT NOT NULL REFERENCES playlists(id),
    track_id BIGINT NOT NULL REFERENCES tracks(id),
    position INTEGER NOT NULL,
    added_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (playlist_id, track_id),
    UNIQUE (playlist_id, position)
);

-- Play Events table
CREATE TABLE play_events (
    id BIGSERIAL PRIMARY KEY,
    session_id UUID NOT NULL,
    user_id BIGINT REFERENCES users(id),
    track_id BIGINT NOT NULL REFERENCES tracks(id),
    playlist_id BIGINT REFERENCES playlists(id),
    event_type VARCHAR(20) NOT NULL,
    event_time TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create indexes for play_events
CREATE INDEX idx_play_events_user_time ON play_events (user_id, event_time DESC);
CREATE INDEX idx_play_events_user_playlist_time ON play_events (user_id, playlist_id, event_time DESC);

-- User Track Reactions table
CREATE TABLE user_track_reactions (
    user_id BIGINT NOT NULL REFERENCES users(id),
    track_id BIGINT NOT NULL REFERENCES tracks(id),
    reaction_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, track_id)
);
```

## Running the Application

1. **Start Redis** (if not using Docker):
   ```bash
   redis-server
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`.

3. **Using Docker Compose** (if configured):
   ```bash
   docker-compose up
   ```

## API Endpoints

The API provides RESTful endpoints for various functionalities. Some endpoints allow anonymous access for basic playback, while others require authentication for personalized features. Below is a brief overview:

- **Authentication** (Optional for basic playback):
  - `POST /api/auth/login` - User login
  - `POST /api/auth/register` - User registration

- **Users** (Requires authentication):
  - `GET /api/users/{id}` - Get user details
  - `PUT /api/users/{id}` - Update user

- **Tracks** (Anonymous access allowed):
  - `GET /api/tracks` - List tracks
  - `GET /api/tracks/{id}` - Get track details
  - `POST /api/tracks` - Add new track (Requires authentication)

- **Albums** (Anonymous access allowed):
  - `GET /api/albums` - List albums
  - `GET /api/albums/{id}` - Get album details

- **Artists** (Anonymous access allowed):
  - `GET /api/artists` - List artists
  - `GET /api/artists/{id}` - Get artist details

- **Playlists** (Requires authentication):
  - `GET /api/playlists` - List user playlists
  - `POST /api/playlists` - Create playlist
  - `PUT /api/playlists/{id}` - Update playlist

- **Play Events** (Anonymous access allowed for recording plays):
  - `POST /api/play-events` - Record play event

- **Reactions** (Requires authentication):
  - `POST /api/reactions` - Add reaction to track

For detailed API documentation, refer to the Swagger UI at `http://localhost:8080/swagger-ui.html` (if configured).

## Testing

Run tests using Maven:

```bash
mvn test
```

The project includes unit tests and integration tests for controllers, services, and repositories.

## Deployment

1. **Build the JAR**:
   ```bash
   mvn clean package
   ```

2. **Run the JAR**:
   ```bash
   java -jar target/music-0.0.1-SNAPSHOT.jar
   ```

For production deployment, consider using Docker or cloud services like AWS, Heroku, or Kubernetes.

## Contributing

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/new-feature`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature/new-feature`).
5. Create a Pull Request.

Please ensure code follows the project's coding standards and includes tests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Frontend

The frontend for Aether Music is available at [https://github.com/devendrahere/aether-music-frontend](https://github.com/devendrahere/aether-music-frontend). It provides a user-friendly interface for interacting with this backend API.
