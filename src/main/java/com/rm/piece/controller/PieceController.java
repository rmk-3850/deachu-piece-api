package com.rm.piece.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rm.exception.CommonResponse;
import com.rm.piece.dto.PieceResponse;
import com.rm.piece.service.PieceService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/pieces")
@RequiredArgsConstructor
public class PieceController {
    private final PieceService service;

    @GetMapping
    public ResponseEntity<CommonResponse<Page<PieceResponse>>> findAll(Pageable pageable) {
        CommonResponse<Page<PieceResponse>> response=service.findAll(pageable);
        return ResponseEntity.status(response.status()).body(response);
    }
    
}
