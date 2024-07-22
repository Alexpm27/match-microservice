package com.example.matchservice.persistence.repositories;

import com.example.matchservice.persistence.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMatchRepository extends JpaRepository<Match, Long> {
    @Query("SELECT m FROM Match m WHERE m.user1Id = :userId OR m.user2Id = :userId")
    List<Match> findAllMatchesByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Match m WHERE m.user1Id = :user1Id AND m.user2Id = :user2Id OR " +
            "(m.user1Id = :user2Id AND m.user2Id = :user1Id)")
    void deleteByUserId(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

}
