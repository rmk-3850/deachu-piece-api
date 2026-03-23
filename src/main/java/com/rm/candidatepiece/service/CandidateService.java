package com.rm.candidatepiece.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rm.candidatepiece.entity.CandidatePiece;
import com.rm.exception.PreliminarySessionDoesntExistException;
import com.rm.piece.dto.CandidateWithPieceDto;
import com.rm.votesession.dto.VoteSessionResponseForVote;
import com.rm.votesession.dto.VoteSessionResponseForVoteResult;
import com.rm.votesession.entity.VoteSession;
import com.rm.votesession.repository.VoteSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CandidateService {
    private final Clock clock;
    private final VoteSessionRepository voteSessionRepository;
    
    public VoteSessionResponseForVote getCandidates(){
        VoteSession current = voteSessionRepository.findByIsActiveTrue()
            .orElseThrow(() -> new PreliminarySessionDoesntExistException());
        List<CandidatePiece> candidates = current.getCandidates();
        List<CandidateWithPieceDto> list = candidates.stream()
            .map(CandidateWithPieceDto::from)
            .toList();
        LocalDateTime now=LocalDateTime.now(clock);
        return new VoteSessionResponseForVote(
            current.getId(),
            current.getTerm(),
            current.getVoteType(),
            current.getEndDate(),
            now,
            list);
    }

    public Slice<VoteSessionResponseForVoteResult> getVoteResults(Pageable pageable){
        VoteSession current = voteSessionRepository.findByIsActiveTrue()
            .orElseThrow(() -> new PreliminarySessionDoesntExistException());
        Slice<VoteSession> sessionPage = voteSessionRepository
            .findNotActiveWithCandidates(current.getEndDate(), pageable);
        
        return sessionPage.map(session -> {
                List<CandidateWithPieceDto> candidateList = session.getCandidates().stream()
                    .map(CandidateWithPieceDto::from)
                    .toList();
                return new VoteSessionResponseForVoteResult(
                    session.getId(), 
                    session.getTerm(),
                    session.getVoteType(),
                    candidateList);
            });
    }
}
