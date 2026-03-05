package com.rm.votesession.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rm.candidatepiece.entity.CandidatePiece;
import com.rm.exception.FinalSessionDoesntExistException;
import com.rm.exception.NoMorePieceLeftException;
import com.rm.exception.PreliminarySessionDoesntExistException;
import com.rm.piece.entity.Piece;
import com.rm.piece.repository.PieceRepository;
import com.rm.votesession.VoteType;
import com.rm.votesession.entity.VoteSession;
import com.rm.votesession.repository.VoteSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteSessionService {
    private final Clock clock;
    private final PieceRepository pieceRepository;
    private final VoteSessionRepository voteSessionRepository;

    private String makeTerm(LocalDate localDate, VoteType voteType){
        return String.format("%d년 %d월 %s 투표", 
            localDate.getYear(), 
            localDate.getMonthValue(),
            (voteType == VoteType.PRELIMINARY ? "예비" : "본선"));
    }
    @Scheduled(cron = "0 */10 0 * * MON")
    @Transactional
    public void checkAndRotateSession(){
        log.info("투표 세션 로테이션 체크 시작");
        // 1. 현재 진행중인 세션 확인
        VoteSession current=voteSessionRepository.findByIsActiveTrue().orElse(null);
        // 2. 만약 세션이 아예 없으면 (최초 시작) 예비 투표 생성
        if(current==null){
            createNewPreliminarySession();
            return;
        }
        // 3. 오늘이 종료일인지 확인 (현재 날짜가 종료일보다 같거나 크면)
        if(!LocalDate.now(clock).isBefore(current.getEndDate())){
            //본투표 세션 생성
            if(current.getVoteType() == VoteType.PRELIMINARY) createNewFinalSession();
            //예비투표 세션 생성
            else {
                handleFinalSession();
                createNewPreliminarySession();
            }
        }
        log.info("투표 세션 로테이션 체크 완료");
    }
    
    @Transactional
    public void createNewPreliminarySession(){
        // 1. 가중치 랜덤으로 5개 추출
        List<Piece> pieces=pieceRepository.findCandidateByWeightedRandom(5);
        // 2. 만약 영상이 5개 미만일 경우 예외처리
        if(pieces.size()<5){
            throw new NoMorePieceLeftException();
        }
        // 3. 선정된 5개 영상을 후보로 등록
        List<CandidatePiece> candidates=pieces.stream()
        .map(piece->{
            piece.updateWeight();
            return piece;
        })
        .map(CandidatePiece::new)
        .toList();
        LocalDate now=LocalDate.now(clock);
        // 4. 신규 예비 투표 세션 생성 (2주 기간)
        VoteSession newSession=VoteSession.builder()
        .term(makeTerm(now,VoteType.PRELIMINARY))
        .voteType(VoteType.PRELIMINARY)
        .startDate(now)
        .endDate(now.plusWeeks(2))
        .isActive(true)
        .build();
        candidates.forEach(newSession::addCandidate);
        voteSessionRepository.save(newSession);
        log.info("새로운 예비 투표 세션 생성 완료: ID={}, 기간={}~{}",
        newSession.getId(), newSession.getStartDate(), newSession.getEndDate());
    }
    
    @Transactional
    public void handleFinalSession(){
        // 1. 현재 진행중인 세션 확인
        VoteSession current=voteSessionRepository.findByIsActiveTrue().orElse(null);
        if(current==null || current.getVoteType()!=VoteType.FINAL){
            throw new FinalSessionDoesntExistException();
        }
        // 2. 본투표 대작 선정
        List<CandidatePiece> candidates=current.getCandidates();
        Long totalVoteCount=0L;
        for(CandidatePiece c : candidates){
            totalVoteCount+=c.getVoteCount();
        }
        CandidatePiece masterPiece=null;
        for(CandidatePiece c : candidates){
            if(c.getVoteCount() > totalVoteCount/2){
                masterPiece=c;
                break;
            }
        }
        if(masterPiece!=null){
            masterPiece.getPiece().updateMasterpiece(true);
        }
        // 3. 활성 세션 비활성화
        voteSessionRepository.deactivateAllActiveSessions();
    }
    
    @Transactional
    public void createNewFinalSession(){
        // 1.현재 진행중인 세션 확인
        VoteSession current=voteSessionRepository.findByIsActiveTrue().orElse(null);
        if(current==null || current.getVoteType()!=VoteType.PRELIMINARY){
            throw new PreliminarySessionDoesntExistException();
        }
        // 2. 본투표 후보 선정
        List<CandidatePiece> preliminaryCandidates=current.getCandidates();
        preliminaryCandidates.sort(Comparator.comparing(CandidatePiece::getVoteCount).reversed());        
        // 3. 선정된 2개 영상을 후보로 등록
        List<Piece> pieces=preliminaryCandidates.subList(0, 2).stream()
            .map(CandidatePiece::getPiece)
            .toList();
        List<CandidatePiece> finalCandidates=pieces.stream()
            .map(piece->{
                piece.updateWeight();
                return piece;
            })
            .map(CandidatePiece::new)
            .toList();
        LocalDate now=LocalDate.now(clock);
        // 4. 활성 세션 비활성화
        voteSessionRepository.deactivateAllActiveSessions();
        // 5. 신규 본투표 세션 생성 (4주 기간)
        VoteSession newSession=VoteSession.builder()
            .term(makeTerm(now,VoteType.FINAL))
            .voteType(VoteType.FINAL)
            .startDate(now)
            .endDate(now.plusWeeks(4))
            .isActive(true)
            .build();
        finalCandidates.forEach(newSession::addCandidate);
        voteSessionRepository.save(newSession);
        log.info("새로운 본 투표 세션 생성 완료: ID={}, 기간={}~{}",
        newSession.getId(), newSession.getStartDate(), newSession.getEndDate());
    }

    
}
