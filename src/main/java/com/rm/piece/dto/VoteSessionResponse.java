package com.rm.piece.dto;

import java.util.List;

import com.rm.votesession.VoteType;

public record VoteSessionResponse(
    Long id,
    String term,
    VoteType voteType,
    List<CandidateWithPieceDto> list
) {

}
