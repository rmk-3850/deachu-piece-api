package com.rm.votesession.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rm.piece.dto.CandidateWithPieceDto;
import com.rm.votesession.VoteType;

public record VoteSessionResponseForVote(
    Long id,
    String term,
    VoteType voteType,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    LocalDateTime endDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    LocalDateTime servernow,
    List<CandidateWithPieceDto> list
) {

}
