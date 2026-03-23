package com.rm.votesession.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rm.votesession.entity.VoteSession;

import jakarta.persistence.LockModeType;


public interface VoteSessionRepository extends JpaRepository<VoteSession, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<VoteSession> findByIsActiveTrue();



    @Query(value = """
        SELECT v FROM VoteSession v
        JOIN FETCH v.candidates c
        JOIN FETCH c.piece
        WHERE v.endDate < :endDate
        ORDER BY v.endDate DESC
    """)
    Slice<VoteSession> findNotActiveWithCandidates(
        @Param("endDate") LocalDateTime endDate,Pageable pageable);

    @Modifying
    @Query(value = "UPDATE VoteSession v SET v.isActive=false WHERE v.isActive=true")
    int deactivateAllActiveSessions();
}
