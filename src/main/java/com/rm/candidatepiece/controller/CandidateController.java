package com.rm.candidatepiece.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rm.candidatepiece.service.CandidateService;
import com.rm.exception.CommonResponse;
import com.rm.exception.PieceSuccess;
import com.rm.votesession.dto.VoteSessionResponseForVote;
import com.rm.votesession.dto.VoteSessionResponseForVoteResult;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @GetMapping("/candidates")
    public ResponseEntity<CommonResponse<VoteSessionResponseForVote>> getCandidates() {
        VoteSessionResponseForVote response = candidateService.getCandidates();
        return ResponseEntity.status(PieceSuccess.SUCCESS.getStatus())
            .body(CommonResponse.success(PieceSuccess.SUCCESS, response));
    }
    
    @GetMapping("/results")
    public ResponseEntity<CommonResponse<Slice<VoteSessionResponseForVoteResult>>> getVoteResult(Pageable pageable){
        Slice<VoteSessionResponseForVoteResult> response = candidateService.getVoteResults(pageable);
        return ResponseEntity.status(PieceSuccess.SUCCESS.getStatus())
            .body(CommonResponse.success(PieceSuccess.SUCCESS, response));
    }
}
