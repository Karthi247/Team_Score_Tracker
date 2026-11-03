package com.scoretracker.repository;

import com.scoretracker.entity.ScoreEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ScoreEventRepository extends JpaRepository<ScoreEvent, Long> {
    List<ScoreEvent> findByMatchIdOrderByTimestampDesc(Long matchId);
    List<ScoreEvent> findByMatchIdOrderByTimestampAsc(Long matchId);

    @Query("SELECT COALESCE(SUM(s.points), 0) FROM ScoreEvent s WHERE s.match.id = :matchId AND s.team.id = :teamId")
    int sumPointsByMatchAndTeam(@Param("matchId") Long matchId, @Param("teamId") Long teamId);
}
