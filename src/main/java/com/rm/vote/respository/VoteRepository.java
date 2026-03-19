package com.rm.vote.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rm.vote.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote,Long>{
    boolean existsByVoteSessionIdAndUserId(Long voteSessionId,Long userId);
    long countByVoteSessionId(Long voteSessionId);
    long countByVoteSessionIdAndCandidateId(Long voteSessionId,Long candidateId);
}
