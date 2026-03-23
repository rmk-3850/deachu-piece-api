package com.rm.votesession.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.rm.candidatepiece.entity.CandidatePiece;
import com.rm.votesession.VoteType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "vote_session",
	indexes = {
		@Index(name = "idx_end_date", columnList = "end_date")
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteSession {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String term;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "vote_type",nullable = false)
	private VoteType voteType;
	
	@Column(name = "start_date",nullable = false)
	private LocalDateTime startDate;
	
	@Column(name = "end_date",nullable = false)
	private LocalDateTime endDate;
	
	@Column(name = "is_active",nullable = false)
	private boolean isActive=false;
	
	@OneToMany(mappedBy = "voteSession", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<CandidatePiece> candidates=new ArrayList<CandidatePiece>();
	
	@Builder
	public VoteSession(String term,VoteType voteType,LocalDateTime startDate,LocalDateTime endDate,boolean isActive) {
		this.term=term;
		this.voteType=voteType;
		this.startDate=startDate;
		this.endDate=endDate;
		this.isActive=isActive;
	}
	public void addCandidate(CandidatePiece candidatePiece){
		this.candidates.add(candidatePiece);
		candidatePiece.setVoteSession(this);
	}
}
