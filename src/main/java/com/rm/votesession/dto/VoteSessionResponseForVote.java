package com.rm.votesession.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.rm.piece.dto.CandidateWithPieceDto;
import com.rm.votesession.VoteType;

public record VoteSessionResponseForVote(
    Long id,
    String term,
    VoteType voteType,
    LocalDateTime endDateTime,
    LocalDateTime servernow,
    List<CandidateWithPieceDto> list
) {

}
