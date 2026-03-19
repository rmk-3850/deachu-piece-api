ALTER TABLE vote_session
ADD COLUMN active_true TINYINT
    GENERATED ALWAYS AS (
        CASE WHEN is_active = 1 THEN 1 ELSE NULL END
    ) STORED;

CREATE UNIQUE INDEX ux_vote_session_active_true
ON vote_session (active_true);