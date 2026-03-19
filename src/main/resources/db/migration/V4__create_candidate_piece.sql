CREATE TABLE candidate_piece (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    piece_id BIGINT NOT NULL,
    vote_count BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT uk_session_piece UNIQUE (session_id,piece_id),

    CONSTRAINT fk_candidate_piece_vote_session
        FOREIGN KEY (session_id)
        REFERENCES vote_session(id),
    
    CONSTRAINT fk_candidate_piece_piece
        FOREIGN KEY (piece_id)
        REFERENCES piece(id)
);