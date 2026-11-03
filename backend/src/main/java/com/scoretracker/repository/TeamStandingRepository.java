package com.scoretracker.repository;

import com.scoretracker.entity.TeamStanding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TeamStandingRepository extends JpaRepository<TeamStanding, Long> {
    
    @Query("SELECT s FROM TeamStanding s WHERE s.tournament.id = :tournamentId " +
           "ORDER BY s.tournamentPoints DESC, s.scoreDifference DESC, s.pointsScored DESC, s.team.name ASC")
    List<TeamStanding> findByTournamentIdOrderByStandings(@Param("tournamentId") Long tournamentId);

    Optional<TeamStanding> findByTournamentIdAndTeamId(Long tournamentId, Long teamId);

    void deleteByTournamentId(Long tournamentId);
}
