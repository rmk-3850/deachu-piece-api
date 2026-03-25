package com.rm.piece.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rm.piece.entity.Piece;
import com.rm.piece.ApprovalStatus;


public interface PieceRepository extends JpaRepository<Piece, Long> {
	Page<Piece> findAllByStatus(Pageable pageable,ApprovalStatus status);
	Optional<Piece> findByYoutubeVideoId(String videoId);
	
	@Query(value = """
	SELECT * FROM piece
	WHERE is_masterpiece=false
	AND status='APPROVED'
	ORDER BY (RAND() * (weight + 1)) DESC
	LIMIT :count
	""",nativeQuery = true)
	List<Piece> findCandidateByWeightedRandom(@Param("count")int count);
}
