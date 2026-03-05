package com.rm.piece.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rm.exception.CommonResponse;
import com.rm.exception.PieceSuccess;
import com.rm.piece.dto.PieceResponse;
import com.rm.piece.repository.PieceRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(readOnly = true)
public class PieceService {

	private final PieceRepository pieceRepository;

	public PieceService(PieceRepository pieceRepository) {
		this.pieceRepository = pieceRepository;
	}

	public CommonResponse<Page<PieceResponse>> findAll(Pageable pageable) {
		return CommonResponse.success(PieceSuccess.SUCCESS,pieceRepository.findAll(pageable)
			.map(PieceResponse::from));
	}
}
