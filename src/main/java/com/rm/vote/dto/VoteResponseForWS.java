package com.rm.vote.dto;

public record VoteResponseForWS(
    Long candidateId,
    Long voteCount
) {

}
