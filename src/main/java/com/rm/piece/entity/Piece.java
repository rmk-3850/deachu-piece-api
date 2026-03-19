package com.rm.piece.entity;

import java.time.LocalDate;

import com.rm.piece.ApprovalStatus;
import com.rm.youtube.VideoType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Piece {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "youtube_video_id",nullable = false,unique = true)
    private String youtubeVideoId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "thumbnail_url",nullable = false)
    private String thumbnailUrl;

    @Column(name = "published_at",nullable = false)
    private LocalDate publishedAt;
    
    @Column(name = "video_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private VideoType videoType;
    
    @Column(name = "view_count",nullable = false)
    private Long viewCount=0L;

    @Column(name = "like_count",nullable = false)
    private Long likeCount=0L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status=ApprovalStatus.PENDING;
    
    @Column(name = "is_masterpiece",nullable = false)
    private boolean isMasterpiece=false;
    
    private Integer weight=0;
    
    public Piece(String youtubeVideoId,String title,String description,
        String thumbnailUrl,LocalDate publishedAt,VideoType videoType,
        Long viewCount,Long likeCount) {
    	this.youtubeVideoId=youtubeVideoId;
    	this.title=title;
    	this.description=description;
    	this.thumbnailUrl=thumbnailUrl;
    	this.publishedAt=publishedAt;
        this.videoType=videoType;
        this.viewCount=viewCount;
        this.likeCount=likeCount;
    }
    public void updateWeight() {
    	this.weight++;
    }
    public void updateStatus(ApprovalStatus status) {
    	this.status=status;
    }
    public void updateMasterpiece(boolean isMasterpiece) {
    	this.isMasterpiece=isMasterpiece;
    }

    public void updateDetails(String title, String description, String thumbnailUrl,
        LocalDate publishedAt,VideoType videoType,
        Long viewCount,Long likeCount) {
    	this.title = title;
    	this.description = description;
    	this.thumbnailUrl = thumbnailUrl;
    	this.publishedAt = publishedAt;
        this.videoType = videoType;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }
}
