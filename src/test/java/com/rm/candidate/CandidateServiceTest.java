package com.rm.candidate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rm.candidatepiece.entity.CandidatePiece;
import com.rm.candidatepiece.repository.CandidatePieceRepository;
import com.rm.candidatepiece.service.CandidateService;
import com.rm.piece.dto.VoteSessionResponse;
import com.rm.piece.entity.Piece;
import com.rm.votesession.VoteType;
import com.rm.votesession.entity.VoteSession;
import com.rm.votesession.repository.VoteSessionRepository;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {
    @Mock
    private VoteSessionRepository voteSessionRepository;

    @Mock
    private CandidatePieceRepository candidatePieceRepository;

    @InjectMocks
    private CandidateService candidateService;

    @Test
    void 활성세션이_없으면_null을_반환한다() {
        // given
        given(voteSessionRepository.findByIsActiveTrue())
                .willReturn(Optional.empty());
        // when
        VoteSessionResponse result = candidateService.getCandidates();
        // then
        assertNull(result);
    }

    @Test
    void 활성세션이_있으면_후보목록을_반환한다() {
        // given
        VoteSession session = mock(VoteSession.class);
        given(voteSessionRepository.findByIsActiveTrue()).willReturn(Optional.of(session));
        given(session.getId()).willReturn(1L);
        CandidatePiece cPiece1 = mock(CandidatePiece.class);
        Piece piece1 = new Piece("video1Id", "video1", null, null, null, null, null, null);
        given(cPiece1.getPiece()).willReturn(piece1);
        CandidatePiece cPiece2 = mock(CandidatePiece.class);        
        Piece piece2 = new Piece("video2Id", "video2", null, null, null, null, null, null);
        given(cPiece2.getPiece()).willReturn(piece2);
        given(session.getCandidates()).willReturn(List.of(cPiece1, cPiece2));

        // when
        VoteSessionResponse result = candidateService.getCandidates();

        // then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(2, result.list().size());
    }
}
