package com.rm.youtube;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.rm.exception.YoutubeSynchornizationException;
import com.rm.piece.entity.Piece;
import com.rm.piece.repository.PieceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeSyncService {
    private final PieceRepository pieceRepository;
    
    @Value("${google.api.key}")
    private String apiKey;
    
    @Value("${youtube.channel.id}")
    private String channelId;

    @Scheduled(cron = "0 0 3 * * *")
    public void scheduleYoutubeSync(){
        log.info(">>> [정기 동기화] 유튜브 데이터 동기화 시작 (channelId: {})",channelId);
        syncAllVideos(channelId);
        log.info(">>> [정기 동기화] 유튜브 데이터 동기화 완료");
    }

    private VideoType checkVideoType(String title, String description) {
        // 대소문자 구분 없이 #shorts 가 포함되어 있는지 확인
        if (title.toLowerCase().contains("#shorts") || 
            description.toLowerCase().contains("#shorts")) {
            return VideoType.SHORTS;
        }        
        return VideoType.LONG;
    }

    private List<List<String>> partition(List<String> list, int size){
        List<List<String>> result=new ArrayList<>();
        for(int i=0;i<list.size();i+=size){
            result.add(list.subList(i, Math.min(i+size, list.size())));
        }
        return result;
    }

    @Transactional
    public void syncAllVideos(String channelId){
        try {            
            YouTube youtube=new YouTube.Builder(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    null)
                .setApplicationName("deachu-project")
                .build();
    
            String uploadPlayListId="UU"+channelId.substring(2);
            String nextPageToken=null;
            List<String> videoIds = new ArrayList<>();
            do {
                var request=youtube.playlistItems()
                    .list(List.of("snippet","contentDetails"))
                    .setPlaylistId(uploadPlayListId)
                    .setMaxResults(50L)
                    .setPageToken(nextPageToken)
                    .setKey(apiKey);
                
                PlaylistItemListResponse response = request.execute();
    
                for(PlaylistItem item : response.getItems()){
                    String videoId = item.getContentDetails().getVideoId();
                    videoIds.add(videoId);
                }    
                nextPageToken=response.getNextPageToken();
            } while (nextPageToken != null);
            List<List<String>> videoIdLists = partition(videoIds, 50);
            for(List<String> videoIdList : videoIdLists){
                YouTube.Videos.List videoRequest=youtube.videos()
                    .list(List.of("snippet","statistics"))
                    .setId(videoIdList)
                    .setKey(apiKey);
                VideoListResponse videoResponse = videoRequest.execute();
                for(Video video : videoResponse.getItems()){
                    saveOrUpdatePiece(video);
                }
            }

        } catch (IOException e) {
            log.error("채널 [{}] 동기화 실패", channelId, e);
            throw new YoutubeSynchornizationException();
        }
    }

    @Transactional
    public void saveOrUpdatePiece(Video video) {
        String videoId = video.getId();
        // 1. DB에서 기존 데이터를 찾음.
        Optional<Piece> existingPiece = pieceRepository.findByYoutubeVideoId(videoId);
        String title = video.getSnippet().getTitle();
        String description = video.getSnippet().getDescription();
        String thumbnailUrl = video.getSnippet().getThumbnails().getHigh().getUrl();
        OffsetDateTime publishedAt = OffsetDateTime.parse(video.getSnippet().getPublishedAt().toString());
        VideoType videoType = checkVideoType(title, description);
        Long viewCount = video.getStatistics().getViewCount().longValue();
        Long likeCount = video.getStatistics().getLikeCount().longValue();
        if (existingPiece.isPresent()) {
            // 2. [Update] 이미 있다면 최신 정보로 업데이트
            Piece piece = existingPiece.get();
            piece.updateDetails(
                title,
                description,
                thumbnailUrl,
                publishedAt.toLocalDate(),
                videoType,
                viewCount,
                likeCount);
        } else {
            // 3. [Save] 없다면 새로 생성
            Piece piece = new Piece(
                videoId,
                title,
                description,
                thumbnailUrl,
                publishedAt.toLocalDate(),
                videoType,
                viewCount,
                likeCount);
            pieceRepository.save(piece);
        }
    }

}
