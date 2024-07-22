package com.example.matchservice.persistence.repositories;

import com.example.matchservice.persistence.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndAndLikedUserId(Long userId, Long likedUserId);

    List<Like> findAllByUserId(Long userId);

    @Query("SELECT l FROM Like l WHERE l.likedUserId = :likedUserId AND NOT EXISTS " +
            "(SELECT m FROM Match m WHERE (m.user1Id = l.userId AND m.user2Id = l.likedUserId) OR " +
            "(m.user1Id = l.likedUserId AND m.user2Id = l.userId))")
    List<Like> findUnmatchedLikes(@Param("likedUserId") Long likedUserId);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.userId = :userId AND l.likedUserId = :likedUserId")
    void deleteByUserIdAndLikedUserId(@Param("userId") Long userId, @Param("likedUserId") Long likedUserId);

    @Query("SELECT l FROM Like l WHERE l.userId = :userId AND l.likedUserId = :likedUserId")
    Optional<Like> getLike(@Param("userId") Long userId, @Param("likedUserId") Long likedUserId);
}
