package com.rm.piece.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rm.exception.CommonResponse;
import com.rm.exception.PieceNotFoundException;
import com.rm.exception.PieceSuccess;
import com.rm.piece.ApprovalStatus;
import com.rm.piece.dto.PieceResponse;
import com.rm.piece.entity.Piece;
import com.rm.piece.repository.PieceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PieceService {

	private final PieceRepository pieceRepository;

	public PieceService(PieceRepository pieceRepository) {
		this.pieceRepository = pieceRepository;
	}

	public CommonResponse<Page<PieceResponse>> findAllPieces(Pageable pageable, ApprovalStatus status) {
		return CommonResponse.success(PieceSuccess.SUCCESS,pieceRepository.findAllByStatus(pageable, status)
			.map(PieceResponse::from));
	}

	@Transactional
	public void updateStatus(Long id, ApprovalStatus status){
		Piece piece = pieceRepository.findById(id)
			.orElseThrow(()-> new PieceNotFoundException());		
		piece.updateStatus(status);
	}
}
