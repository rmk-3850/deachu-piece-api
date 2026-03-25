package com.rm.vote.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rm.exception.CommonResponse;
import com.rm.exception.PieceSuccess;
import com.rm.vote.dto.VoteRequest;
import com.rm.vote.dto.VoteResponseForWS;
import com.rm.vote.service.VoteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService service;
    private final SimpMessagingTemplate messagingTemplate;
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<Void>> vote(
        @AuthenticationPrincipal String id,
        @RequestBody VoteRequest request
    ) {
        VoteResponseForWS response = service.vote(request.voteSessionId(), Long.parseLong(id), request.candidateId());

        messagingTemplate.convertAndSend("/topic/vote", response);

        return ResponseEntity.ok(new CommonResponse<>(
            true,
            PieceSuccess.SUCCESS.getStatus(),
            PieceSuccess.SUCCESS.getCode(),
            PieceSuccess.SUCCESS.getMsg(),
            null));
    }
    
}
