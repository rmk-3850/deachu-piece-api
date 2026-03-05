package com.rm.piece.dto;

import java.time.LocalDate;

import com.rm.piece.ApprovalStatus;
import com.rm.piece.entity.Piece;
import com.rm.youtube.VideoType;

public record PieceResponse(
	Long id,
	String youtubeVideoId,
	String title,
	String description,
	String thumbnailUrl,
	LocalDate publishedAt,
	VideoType videoType,
	ApprovalStatus status,
	Long viewCount,
	Long likeCount,
	boolean isMasterpiece
	){
	public static PieceResponse from(Piece piece) {
		return new PieceResponse(
			piece.getId(),
			piece.getYoutubeVideoId(),
			piece.getTitle(),
			piece.getDescription(),
			piece.getThumbnailUrl(),
			piece.getPublishedAt(),
			piece.getVideoType(),
			piece.getStatus(),
			piece.getViewCount(),
			piece.getLikeCount(),
			piece.isMasterpiece()
		);
	}
}
