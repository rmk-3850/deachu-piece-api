package com.rm.candidatepiece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rm.candidatepiece.entity.CandidatePiece;

public interface CandidatePieceRepository extends JpaRepository<CandidatePiece, Long>{
    @Modifying
    @Query("""
        UPDATE Candidate c
        SET c.voteCount = c.voteCount+1
        WHERE c.id = :candidateId
    """)
    void incrementVoteCount(Long candidateId);
}
