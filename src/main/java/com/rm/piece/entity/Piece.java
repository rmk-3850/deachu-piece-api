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
	
	@Column(nullable = false,unique = true)
    private String youtubeVideoId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private LocalDate publishedAt;
    
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private VideoType videoType;

    private Long viewCount=0L;

    private Long likeCount=0L;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status=ApprovalStatus.PENDING;
    
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
