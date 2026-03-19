CREATE TABLE vote (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vote_session_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,

    CONSTRAINT uk_vote_session_uid UNIQUE (vote_session_id, user_id)
);

CREATE INDEX idx_vote_session ON vote (vote_session_id);
CREATE INDEX idx_vote_session_candidate ON vote (vote_session_id, candidate_id);