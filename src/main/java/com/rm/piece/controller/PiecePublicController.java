package com.rm.piece.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rm.exception.CommonResponse;
import com.rm.piece.ApprovalStatus;
import com.rm.piece.dto.PieceResponse;
import com.rm.piece.service.PieceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PiecePublicController {
    private final PieceService service;

    @GetMapping("/pieces")
    public ResponseEntity<CommonResponse<Page<PieceResponse>>> findAllPiecesApproved(Pageable pageable) {
        CommonResponse<Page<PieceResponse>> response=service.findAllPieces(pageable, ApprovalStatus.APPROVED);
        return ResponseEntity.status(response.status()).body(response);
    }
}
