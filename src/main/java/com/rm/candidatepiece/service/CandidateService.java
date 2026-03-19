package com.rm.candidatepiece.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rm.candidatepiece.entity.CandidatePiece;
import com.rm.exception.PreliminarySessionDoesntExistException;
import com.rm.piece.dto.CandidateWithPieceDto;
import com.rm.piece.dto.VoteSessionResponse;
import com.rm.votesession.entity.VoteSession;
import com.rm.votesession.repository.VoteSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CandidateService {
    private final VoteSessionRepository voteSessionRepository;
    
    public VoteSessionResponse getCandidates(){
        VoteSession current = voteSessionRepository.findByIsActiveTrue()
            .orElseThrow(() -> new PreliminarySessionDoesntExistException());
        List<CandidatePiece> candidates = current.getCandidates();
        List<CandidateWithPieceDto> list = candidates.stream()
            .map(CandidateWithPieceDto::from)
            .toList();

        return new VoteSessionResponse(
            current.getId(),
            current.getTerm(),
            current.getVoteType(),
            list);
    }

    public Slice<VoteSessionResponse> getVoteResults(Pageable pageable){
        VoteSession current = voteSessionRepository.findByIsActiveTrue()
            .orElseThrow(() -> new PreliminarySessionDoesntExistException());
        Slice<VoteSession> sessionPage = voteSessionRepository
            .findNotActiveWithCandidates(current.getEndDate(), pageable);
        
        return sessionPage.map(session -> {
                List<CandidateWithPieceDto> candidateList = session.getCandidates().stream()
                    .map(CandidateWithPieceDto::from)
                    .toList();
                return new VoteSessionResponse(
                    session.getId(), 
                    session.getTerm(),
                    session.getVoteType(),
                    candidateList);
            });
    }
}
