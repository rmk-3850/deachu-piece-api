package com.rm.candidatepiece.entity;

import com.rm.piece.entity.Piece;
import com.rm.votesession.entity.VoteSession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
	name = "candidate_piece",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_session_piece",
		columnNames = {"session_id","piece_id"}
	),
	indexes = {
		@Index(name = "idx_candidate_piece_session_id", columnList = "session_id")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CandidatePiece {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id",nullable = false)
	private VoteSession voteSession;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "piece_id",nullable = false)
	private Piece piece;
	
	@Column(name = "vote_count",nullable = false)
	private Long voteCount=0L;
	
	public CandidatePiece(Piece piece) {
		this.piece=piece;
	}
	public void setVoteSession(VoteSession voteSession){
		this.voteSession=voteSession;
	}
}
