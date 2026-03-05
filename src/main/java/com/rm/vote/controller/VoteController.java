package com.rm.vote.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rm.exception.CommonResponse;
import com.rm.exception.PieceSuccess;
import com.rm.vote.service.VoteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService service;
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<Void>> vote(@RequestParam String param) {
        service.vote(null, null, null);
        CommonResponse<Void> response= new CommonResponse<>(
            true,
            PieceSuccess.SUCCESS.getStatus(),
            PieceSuccess.SUCCESS.getCode(),
            PieceSuccess.SUCCESS.getMsg(),
            null);
        return ResponseEntity.ok(response);
    }
    
}
