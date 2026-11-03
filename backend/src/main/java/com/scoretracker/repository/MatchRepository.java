package com.scoretracker.repository;

import com.scoretracker.entity.Match;
import com.scoretracker.enums.MatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTournamentId(Long tournamentId);
    List<Match> findByStatus(MatchStatus status);

    @Query("SELECT m FROM Match m WHERE m.tournament.id = :tournamentId AND m.status = :status")
    List<Match> findByTournamentIdAndStatus(@Param("tournamentId") Long tournamentId, @Param("status") MatchStatus status);

    @Query("SELECT m FROM Match m WHERE " +
           "(:tournamentId IS NULL OR m.tournament.id = :tournamentId) AND " +
           "(:status IS NULL OR m.status = :status) AND " +
           "(:date IS NULL OR m.matchDate = :date) AND " +
           "(:teamId IS NULL OR m.teamA.id = :teamId OR m.teamB.id = :teamId) " +
           "ORDER BY m.matchDate DESC, m.matchTime DESC")
    Page<Match> searchMatches(@Param("tournamentId") Long tournamentId,
                              @Param("status") MatchStatus status,
                              @Param("date") LocalDate date,
                              @Param("teamId") Long teamId,
                              Pageable pageable);

    @Query("SELECT m FROM Match m WHERE m.status = 'COMPLETED' ORDER BY m.matchDate DESC, m.matchTime DESC")
    List<Match> findRecentCompletedMatches(Pageable pageable);

    @Query("SELECT m FROM Match m WHERE m.status = 'UPCOMING' ORDER BY m.matchDate ASC, m.matchTime ASC")
    List<Match> findUpcomingMatches(Pageable pageable);

    @Query("SELECT m FROM Match m WHERE m.status = 'LIVE' ORDER BY m.matchDate ASC, m.matchTime ASC")
    List<Match> findLiveMatches();

    long countByStatus(MatchStatus status);

    @Query("SELECT COUNT(m) FROM Match m WHERE (m.teamA.id = :teamId OR m.teamB.id = :teamId) AND m.status = 'COMPLETED'")
    long countCompletedMatchesByTeam(@Param("teamId") Long teamId);

    @Query("SELECT COUNT(m) FROM Match m WHERE m.winner.id = :teamId AND m.status = 'COMPLETED'")
    long countWonMatchesByTeam(@Param("teamId") Long teamId);
}
