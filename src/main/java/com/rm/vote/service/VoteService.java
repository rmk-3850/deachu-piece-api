package com.rm.vote.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rm.candidatepiece.repository.CandidatePieceRepository;
import com.rm.exception.AlreadyVotedException;
import com.rm.vote.entity.Vote;
import com.rm.vote.respository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {
    private final VoteRepository voteRepository;
    private final CandidatePieceRepository candidatePieceRepository;
    private boolean isUniqueConstraintViolation(DataIntegrityViolationException e){
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof ConstraintViolationException cve) {
                return "uk_vote_session_uid".equalsIgnoreCase(cve.getConstraintName());
            }
            cause = cause.getCause();
        }
        return false;
    }
    public void vote(Long voteSessionId, Long uid, Long candidateId) {
        //이미 투표했으면 다시 투표할 수 없다.
        if(voteRepository.existsByVoteSessionIdAndUid(voteSessionId, uid)){
            throw new AlreadyVotedException();
        }
        Vote vote=new Vote(voteSessionId, uid, candidateId);
        try {
            voteRepository.save(vote);
            candidatePieceRepository.incrementVoteCount(candidateId);
        } catch (DataIntegrityViolationException e) {
            if (isUniqueConstraintViolation(e)) {
                throw new AlreadyVotedException();
            }
            throw e;
        }
    }

}
