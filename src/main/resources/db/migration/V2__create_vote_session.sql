CREATE TABLE vote_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    term VARCHAR(255) NOT NULL,
    vote_type VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT FALSE
);