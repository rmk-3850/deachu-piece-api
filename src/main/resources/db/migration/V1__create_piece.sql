CREATE TABLE piece (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    youtube_video_id VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    thumbnail_url VARCHAR(255) NOT NULL,
    published_at DATE NOT NULL,
    video_type VARCHAR(20) NOT NULL,
    view_count BIGINT NOT NULL DEFAULT 0,
    like_count BIGINT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    is_masterpiece BOOLEAN NOT NULL DEFAULT FALSE,
    weight INT DEFAULT 0
);