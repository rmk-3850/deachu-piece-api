package com.rm.candidatepiece.entity;

import com.rm.piece.entity.Piece;
import com.rm.votesession.entity.VoteSession;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CandidatePiece {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id")
	private VoteSession voteSession;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "piece_id")
	private Piece piece;
	
	private Long voteCount=0L;
	
	public CandidatePiece(Piece piece) {
		this.piece=piece;
	}
	public void setVoteSession(VoteSession voteSession){
		this.voteSession=voteSession;
	}
}
