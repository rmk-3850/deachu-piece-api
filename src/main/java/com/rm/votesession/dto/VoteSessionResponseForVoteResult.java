package com.rm.votesession.dto;

import java.util.List;

import com.rm.piece.dto.CandidateWithPieceDto;
import com.rm.votesession.VoteType;

public record VoteSessionResponseForVoteResult(
    Long id,
    String term,
    VoteType voteType,
    List<CandidateWithPieceDto> list
) {

}
