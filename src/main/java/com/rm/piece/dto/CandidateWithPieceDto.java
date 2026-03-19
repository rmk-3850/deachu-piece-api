package com.rm.piece.dto;

import com.rm.candidatepiece.entity.CandidatePiece;
import com.rm.piece.entity.Piece;

public record CandidateWithPieceDto(
    Long id,
	Long voteCount,
    Long pieceId,
	String youtubeVideoId,
	String title,
	String description,
	String thumbnailUrl
) {
	public static CandidateWithPieceDto from(CandidatePiece candidatePiece){
		Piece piece = candidatePiece.getPiece();
		return new CandidateWithPieceDto(
			candidatePiece.getId(),
			candidatePiece.getVoteCount(),
			piece.getId(),
			piece.getYoutubeVideoId(),
			piece.getTitle(),
			piece.getDescription(),
			piece.getThumbnailUrl());
	}
}
