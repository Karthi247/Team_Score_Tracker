package com.scoretracker.repository;

import com.scoretracker.entity.TournamentTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TournamentTeamRepository extends JpaRepository<TournamentTeam, Long> {
    List<TournamentTeam> findByTournamentId(Long tournamentId);
    boolean existsByTournamentIdAndTeamId(Long tournamentId, Long teamId);
    Optional<TournamentTeam> findByTournamentIdAndTeamId(Long tournamentId, Long teamId);
    void deleteByTournamentIdAndTeamId(Long tournamentId, Long teamId);
}
