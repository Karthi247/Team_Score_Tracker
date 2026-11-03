package com.scoretracker.service.impl;

import com.scoretracker.dto.standing.StandingResponse;
import com.scoretracker.entity.*;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.exception.ResourceNotFoundException;
import com.scoretracker.mapper.StandingMapper;
import com.scoretracker.repository.*;
import com.scoretracker.service.StandingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StandingServiceImpl implements StandingService {

    private final TeamStandingRepository teamStandingRepository;
    private final TournamentRepository tournamentRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final MatchRepository matchRepository;
    private final StandingMapper standingMapper;

    public StandingServiceImpl(TeamStandingRepository teamStandingRepository,
                               TournamentRepository tournamentRepository,
                               TournamentTeamRepository tournamentTeamRepository,
                               MatchRepository matchRepository,
                               StandingMapper standingMapper) {
        this.teamStandingRepository = teamStandingRepository;
        this.tournamentRepository = tournamentRepository;
        this.tournamentTeamRepository = tournamentTeamRepository;
        this.matchRepository = matchRepository;
        this.standingMapper = standingMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StandingResponse> getStandingsByTournament(Long tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException("Tournament", "id", tournamentId);
        }

        List<TeamStanding> standings = teamStandingRepository.findByTournamentIdOrderByStandings(tournamentId);
        return standings.stream()
                .map(standingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void recalculateStandings(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", tournamentId));

        int winPts = tournament.getWinPoints() != null ? tournament.getWinPoints() : 3;
        int drawPts = tournament.getDrawPoints() != null ? tournament.getDrawPoints() : 1;
        int lossPts = tournament.getLossPoints() != null ? tournament.getLossPoints() : 0;

        // Get all registered teams
        List<TournamentTeam> registeredTeams = tournamentTeamRepository.findByTournamentId(tournamentId);
        Set<Team> teams = registeredTeams.stream().map(TournamentTeam::getTeam).collect(Collectors.toSet());

        // Also check if any matches have teams not in registered list
        List<Match> tournamentMatches = matchRepository.findByTournamentId(tournamentId);
        for (Match m : tournamentMatches) {
            teams.add(m.getTeamA());
            teams.add(m.getTeamB());
        }

        List<Match> completedMatches = tournamentMatches.stream()
                .filter(m -> m.getStatus() == MatchStatus.COMPLETED)
                .toList();

        List<TeamStanding> standingEntities = new ArrayList<>();

        for (Team team : teams) {
            TeamStanding standing = teamStandingRepository.findByTournamentIdAndTeamId(tournamentId, team.getId())
                    .orElse(new TeamStanding(tournament, team));

            int matchesPlayed = 0;
            int wins = 0;
            int draws = 0;
            int losses = 0;
            int pointsScored = 0;
            int pointsConceded = 0;

            for (Match m : completedMatches) {
                if (m.getTeamA().getId().equals(team.getId())) {
                    matchesPlayed++;
                    pointsScored += m.getTeamAScore();
                    pointsConceded += m.getTeamBScore();

                    if (m.getTeamAScore() > m.getTeamBScore()) {
                        wins++;
                    } else if (m.getTeamAScore() < m.getTeamBScore()) {
                        losses++;
                    } else {
                        draws++;
                    }
                } else if (m.getTeamB().getId().equals(team.getId())) {
                    matchesPlayed++;
                    pointsScored += m.getTeamBScore();
                    pointsConceded += m.getTeamAScore();

                    if (m.getTeamBScore() > m.getTeamAScore()) {
                        wins++;
                    } else if (m.getTeamBScore() < m.getTeamAScore()) {
                        losses++;
                    } else {
                        draws++;
                    }
                }
            }

            int scoreDiff = pointsScored - pointsConceded;
            int totalTournamentPoints = (wins * winPts) + (draws * drawPts) + (losses * lossPts);

            standing.setMatchesPlayed(matchesPlayed);
            standing.setWins(wins);
            standing.setDraws(draws);
            standing.setLosses(losses);
            standing.setPointsScored(pointsScored);
            standing.setPointsConceded(pointsConceded);
            standing.setScoreDifference(scoreDiff);
            standing.setTournamentPoints(totalTournamentPoints);
            standing.setUpdatedAt(LocalDateTime.now());

            standingEntities.add(standing);
        }

        // Sort: 1. Points DESC, 2. Score Difference DESC, 3. Points Scored DESC, 4. Team Name ASC
        standingEntities.sort((a, b) -> {
            int compPoints = b.getTournamentPoints().compareTo(a.getTournamentPoints());
            if (compPoints != 0) return compPoints;

            int compDiff = b.getScoreDifference().compareTo(a.getScoreDifference());
            if (compDiff != 0) return compDiff;

            int compScored = b.getPointsScored().compareTo(a.getPointsScored());
            if (compScored != 0) return compScored;

            return a.getTeam().getName().compareToIgnoreCase(b.getTeam().getName());
        });

        // Assign ranks
        for (int i = 0; i < standingEntities.size(); i++) {
            standingEntities.get(i).setPositionRank(i + 1);
        }

        teamStandingRepository.saveAll(standingEntities);
    }
}

