package com.rm.vote;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.rm.candidatepiece.repository.CandidatePieceRepository;
import com.rm.exception.AlreadyVotedException;
import com.rm.vote.entity.Vote;
import com.rm.vote.respository.VoteRepository;
import com.rm.vote.service.VoteService;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private CandidatePieceRepository candidatePieceRepository;
    @InjectMocks
    private VoteService voteService;
    @Test
    void 이미_투표했으면_다시_투표할_수_없다(){
        //given
        when(voteRepository.existsByVoteSessionIdAndUid(1L, 1L))
            .thenReturn(true);
        //when&then
        assertThrows(AlreadyVotedException.class,
            ()->voteService.vote(1L, 1L, 1L)
        );
    }
    @Test
    void UNIQUE_충돌이_나면_AlreadyVotedException을_던진다(){
        //given
        var cause = new ConstraintViolationException(
            "duplicate",
            null,
            "uk_vote_session_uid"
        );
        var ex = new DataIntegrityViolationException("unique", cause);
        when(voteRepository.existsByVoteSessionIdAndUid(1L, 1L))
            .thenReturn(false);
        when(voteRepository.save(any(Vote.class))).thenThrow(ex);
        //when&then
        assertThrows(AlreadyVotedException.class,
            ()->voteService.vote(1L, 1L, 1L)
        );
        verify(candidatePieceRepository,never()).incrementVoteCount(any());
    }
}
