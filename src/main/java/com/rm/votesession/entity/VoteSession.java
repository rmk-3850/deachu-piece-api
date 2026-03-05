package com.rm.votesession.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.rm.candidatepiece.entity.CandidatePiece;
import com.rm.votesession.VoteType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteSession {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String term;
	
	@Enumerated(EnumType.STRING)
	private VoteType voteType;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private boolean isActive=false;
	
	@OneToMany(mappedBy = "voteSession", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<CandidatePiece> candidates=new ArrayList<CandidatePiece>();
	
	@Builder
	public VoteSession(String term,VoteType voteType,LocalDate startDate,LocalDate endDate,boolean isActive) {
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
