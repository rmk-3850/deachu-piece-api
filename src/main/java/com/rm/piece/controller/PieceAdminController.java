package com.rm.piece.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rm.exception.CommonResponse;
import com.rm.exception.PieceSuccess;
import com.rm.piece.ApprovalStatus;
import com.rm.piece.dto.PieceResponse;
import com.rm.piece.service.PieceService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PieceAdminController {
    private final PieceService service;

    @GetMapping("/pieces")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Page<PieceResponse>>> findAllPiecesPending(Pageable pageable) {
        CommonResponse<Page<PieceResponse>> response=service.findAllPieces(pageable, ApprovalStatus.PENDING);
        return ResponseEntity.status(response.status()).body(response);
    }
    
    @PostMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> approve(
        @PathVariable("id") Long id
    ) {
        service.updateStatus(id, ApprovalStatus.APPROVED);        
        return ResponseEntity.status(PieceSuccess.SUCCESS.getStatus())
            .body(CommonResponse.success(PieceSuccess.SUCCESS, null));
    }
    
    @PostMapping("/deny/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> deny(
        @PathVariable("id") Long id
    ) {
        service.updateStatus(id, ApprovalStatus.DENIED);        
        return ResponseEntity.status(PieceSuccess.SUCCESS.getStatus())
            .body(CommonResponse.success(PieceSuccess.SUCCESS, null));
    }
}
