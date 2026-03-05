package com.rm.votesession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rm.candidatepiece.entity.CandidatePiece;
import com.rm.piece.entity.Piece;
import com.rm.piece.repository.PieceRepository;
import com.rm.votesession.entity.VoteSession;
import com.rm.votesession.repository.VoteSessionRepository;
import com.rm.votesession.service.VoteSessionService;

@ExtendWith(MockitoExtension.class)
public class VoteSessionServiceTest {
    @Mock
    private Clock clock;
    @Mock
    private PieceRepository pieceRepository;
    @Mock
    private VoteSessionRepository voteSessionRepository;
    @InjectMocks
    private VoteSessionService voteSessionService;
    @Captor
    private ArgumentCaptor<VoteSession> voteSessionCaptor;
    @Test
    void 활성세션이_없으면_예비투표세션을_생성한다(){
        //given
        LocalDate today=LocalDate.of(2026, 3, 1);
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(voteSessionRepository.findByIsActiveTrue()).thenReturn(Optional.empty());

        List<Piece> pieces=List.of(
            mock(Piece.class),
            mock(Piece.class),
            mock(Piece.class),
            mock(Piece.class),
            mock(Piece.class)
        );
        when(pieceRepository.findCandidateByWeightedRandom(5)).thenReturn(pieces);
        //when
        voteSessionService.checkAndRotateSession();
        //then
        verify(voteSessionRepository).save(any(VoteSession.class));
    }
    @Test
    void 예비투표가_종료되면_본투표를_생성한다(){
        //given
        LocalDate today=LocalDate.of(2026, 3, 1);
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        VoteSession preliminary=VoteSession.builder()
            .voteType(VoteType.PRELIMINARY)
            .startDate(today.minusWeeks(2))
            .endDate(today.minusDays(1))
            .isActive(true)
            .build();
        
        when(voteSessionRepository.findByIsActiveTrue()).thenReturn(Optional.of(preliminary));

        CandidatePiece c1=mock(CandidatePiece.class);
        CandidatePiece c2=mock(CandidatePiece.class);
        CandidatePiece c3=mock(CandidatePiece.class);
        CandidatePiece c4=mock(CandidatePiece.class);
        CandidatePiece c5=mock(CandidatePiece.class);
        when(c1.getVoteCount()).thenReturn(10L);
        when(c2.getVoteCount()).thenReturn(1L);
        when(c3.getVoteCount()).thenReturn(2L);
        when(c4.getVoteCount()).thenReturn(7L);
        when(c5.getVoteCount()).thenReturn(5L);
        Piece p1=mock(Piece.class);
        Piece p4=mock(Piece.class);
        when(c1.getPiece()).thenReturn(p1);
        when(c4.getPiece()).thenReturn(p4);

        preliminary.addCandidate(c1);
        preliminary.addCandidate(c2);
        preliminary.addCandidate(c3);
        preliminary.addCandidate(c4);
        preliminary.addCandidate(c5);

        //when
        voteSessionService.checkAndRotateSession();

        //then
        verify(voteSessionRepository).save(voteSessionCaptor.capture());
        VoteSession saved=voteSessionCaptor.getValue();

        assertThat(saved.getVoteType()).isEqualTo(VoteType.FINAL);
        assertThat(saved.isActive()).isTrue();
        assertThat(saved.getCandidates()).extracting(c->c.getPiece()).containsExactlyInAnyOrder(p1,p4);
    }

    @Test
    void 본투표가_종료되면_대작선정과_예비투표를_생성한다(){
        //given
        LocalDate today=LocalDate.of(2026, 3, 1);
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        VoteSession finalSession=VoteSession.builder()
            .voteType(VoteType.FINAL)
            .startDate(today.minusWeeks(2))
            .endDate(today.minusDays(1))
            .isActive(true)
            .build();
        
        when(voteSessionRepository.findByIsActiveTrue()).thenReturn(Optional.of(finalSession));

        CandidatePiece c1=mock(CandidatePiece.class);
        CandidatePiece c2=mock(CandidatePiece.class);
        when(c1.getVoteCount()).thenReturn(6L);
        when(c2.getVoteCount()).thenReturn(4L);
        Piece p1=mock(Piece.class);
        when(c1.getPiece()).thenReturn(p1);

        finalSession.addCandidate(c1);
        finalSession.addCandidate(c2);

        List<Piece> pieces=List.of(
            mock(Piece.class),
            mock(Piece.class),
            mock(Piece.class),
            mock(Piece.class),
            mock(Piece.class)
        );
        when(pieceRepository.findCandidateByWeightedRandom(5)).thenReturn(pieces);

        //when
        voteSessionService.checkAndRotateSession();

        //then
        verify(voteSessionRepository).save(any(VoteSession.class));
        verify(p1).updateMasterpiece(true);
    }
}
