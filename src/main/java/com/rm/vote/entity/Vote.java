package com.rm.vote.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "vote",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_vote_session_uid",
        columnNames = {"vote_session_id","uid"}
    ),
    indexes = {
        @Index(name="idx_vote_session",columnList = "vote_session_id"),
        @Index(name="idx_vote_session_candidate",columnList = "vote_session_id,candidate_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "vote_session_id", nullable = false)
    private Long voteSessionId;
    @Column(name = "uid", nullable = false)
    private Long uid;
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    public Vote(
        Long voteSessionId,
        Long uid,
        Long candidateId
        ) {
        this.voteSessionId=voteSessionId;
        this.uid=uid;
        this.candidateId=candidateId;
    }
}
