package com.rm.votesession.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rm.votesession.entity.VoteSession;

import jakarta.persistence.LockModeType;

public interface VoteSessionRepository extends JpaRepository<VoteSession, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<VoteSession> findByIsActiveTrue();

    @Modifying
    @Query(value = "UPDATE VoteSession v SET v.isActive=false WHERE v.isActive=true")
    int deactivateAllActiveSessions();
}
