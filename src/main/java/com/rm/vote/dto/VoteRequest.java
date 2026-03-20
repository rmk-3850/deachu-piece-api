package com.rm.vote.dto;

import jakarta.validation.constraints.NotNull;

public record VoteRequest(
    @NotNull Long voteSessionId,
    @NotNull Long candidateId
) {

}
