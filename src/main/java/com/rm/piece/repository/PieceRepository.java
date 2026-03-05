package com.rm.piece.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rm.piece.entity.Piece;

public interface PieceRepository extends JpaRepository<Piece, Long> {
	Optional<Piece> findByYoutubeVideoId(String videoId);

	@Query(value = """
		SELECT p.* FROM piece p
		WHERE p.is_masterpiece=false
		AND p.status='APPROVED'
		ORDER BY (RAND() * (p.weight +1)) DESC
		LIMIT :count
	""",nativeQuery = true)
	List<Piece> findCandidateByWeightedRandom(@Param("count")int count);
}
