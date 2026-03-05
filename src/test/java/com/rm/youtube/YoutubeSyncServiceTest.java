package com.rm.youtube;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;
import com.rm.piece.entity.Piece;
import com.rm.piece.repository.PieceRepository;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class YoutubeSyncServiceTest {

    @Mock
    private PieceRepository pieceRepository;

    @InjectMocks
    private YoutubeSyncService youtubeSyncService;

    @Test
    void 기존_영상이_있으면_update_되어야_한다() {
        // given
        String videoId = "abc123";

        Video video = mock(Video.class);
        VideoSnippet snippet = mock(VideoSnippet.class);
        VideoStatistics statistics = mock(VideoStatistics.class);
        ThumbnailDetails thumbnails = mock(ThumbnailDetails.class);
        Thumbnail high = mock(Thumbnail.class);
        when(video.getId()).thenReturn(videoId);
        when(video.getSnippet()).thenReturn(snippet);
        when(video.getStatistics()).thenReturn(statistics);

        when(snippet.getTitle()).thenReturn("title");
        when(snippet.getDescription()).thenReturn("desc");
        when(snippet.getPublishedAt()).thenReturn(new DateTime("2024-01-01T00:00:00Z"));
        when(snippet.getThumbnails()).thenReturn(thumbnails);
        when(thumbnails.getHigh()).thenReturn(high);
        when(high.getUrl()).thenReturn("thumb");

        when(statistics.getViewCount()).thenReturn(BigInteger.valueOf(100));
        when(statistics.getLikeCount()).thenReturn(BigInteger.valueOf(10));

        Piece existing=mock(Piece.class);
        when(pieceRepository.findByYoutubeVideoId(videoId)).thenReturn(Optional.of(existing));

        // when
        youtubeSyncService.saveOrUpdatePiece(video);

        // then
        verify(existing).updateDetails(
            any(), any(), any(), any(), any(), eq(100L), eq(10L)
        );
        verify(pieceRepository, never()).save(any(Piece.class));
    }

    @Test
void 기존_영상이_없으면_save_된다() {
    // given
    String videoId = "abc123";

    Video video = mock(Video.class);
    VideoSnippet snippet = mock(VideoSnippet.class);
    VideoStatistics statistics = mock(VideoStatistics.class);
    ThumbnailDetails thumbnails = mock(ThumbnailDetails.class);
    Thumbnail high = mock(Thumbnail.class);

    when(video.getId()).thenReturn(videoId);
    when(video.getSnippet()).thenReturn(snippet);
    when(video.getStatistics()).thenReturn(statistics);

    when(snippet.getTitle()).thenReturn("title");
    when(snippet.getDescription()).thenReturn("desc");
    when(snippet.getPublishedAt()).thenReturn(new DateTime("2024-01-01T00:00:00Z"));
    when(snippet.getThumbnails()).thenReturn(thumbnails);
    when(thumbnails.getHigh()).thenReturn(high);
    when(high.getUrl()).thenReturn("thumb");

    when(statistics.getViewCount()).thenReturn(BigInteger.valueOf(100));
    when(statistics.getLikeCount()).thenReturn(BigInteger.valueOf(10));

    when(pieceRepository.findByYoutubeVideoId(videoId))
            .thenReturn(Optional.empty());

    // when
    youtubeSyncService.saveOrUpdatePiece(video);

    // then
    verify(pieceRepository).save(any(Piece.class));
}
}
