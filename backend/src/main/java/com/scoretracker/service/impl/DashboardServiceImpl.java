package com.scoretracker.service.impl;

import com.scoretracker.dto.dashboard.ChartDataPoint;
import com.scoretracker.dto.dashboard.DashboardStatsResponse;
import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.dto.team.TeamStatsResponse;
import com.scoretracker.dto.tournament.TournamentResponse;
import com.scoretracker.entity.Team;
import com.scoretracker.entity.Tournament;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.enums.SportType;
import com.scoretracker.mapper.MatchMapper;
import com.scoretracker.mapper.TournamentMapper;
import com.scoretracker.repository.*;
import com.scoretracker.service.DashboardService;
import com.scoretracker.service.TeamService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final TeamService teamService;
    private final MatchMapper matchMapper;
    private final TournamentMapper tournamentMapper;

    public DashboardServiceImpl(TeamRepository teamRepository,
                                PlayerRepository playerRepository,
                                TournamentRepository tournamentRepository,
                                MatchRepository matchRepository,
                                TeamService teamService,
                                MatchMapper matchMapper,
                                TournamentMapper tournamentMapper) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
        this.teamService = teamService;
        this.matchMapper = matchMapper;
        this.tournamentMapper = tournamentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStatistics() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        // 1. Summary card counts
        stats.setTotalTeams(teamRepository.count());
        stats.setTotalPlayers(playerRepository.count());
        stats.setTotalTournaments(tournamentRepository.count());
        stats.setTotalMatches(matchRepository.count());
        stats.setCompletedMatches(matchRepository.countByStatus(MatchStatus.COMPLETED));
        stats.setUpcomingMatches(matchRepository.countByStatus(MatchStatus.UPCOMING));
        stats.setLiveMatches(matchRepository.countByStatus(MatchStatus.LIVE));

        // 2. Recent & Upcoming & Live Matches
        List<MatchResponse> recent = matchRepository.findRecentCompletedMatches(PageRequest.of(0, 5)).stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());
        stats.setRecentMatches(recent);

        List<MatchResponse> upcoming = matchRepository.findUpcomingMatches(PageRequest.of(0, 5)).stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());
        stats.setUpcomingMatchesList(upcoming);

        List<MatchResponse> live = matchRepository.findLiveMatches().stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());
        stats.setLiveMatchesList(live);

        // 3. Top Performing Teams (sorted by win rate & wins)
        List<Team> allTeams = teamRepository.findAll();
        List<TeamStatsResponse> topTeams = allTeams.stream()
                .map(t -> teamService.getTeamStatistics(t.getId()))
                .sorted((a, b) -> {
                    int winCompare = Integer.compare(b.getWins(), a.getWins());
                    if (winCompare != 0) return winCompare;
                    return Double.compare(b.getWinRate(), a.getWinRate());
                })
                .limit(5)
                .collect(Collectors.toList());
        stats.setTopTeams(topTeams);

        // 4. Recent Tournaments
        List<TournamentResponse> recentTournaments = tournamentRepository.findAll(
                PageRequest.of(0, 4, Sort.by("createdAt").descending())).stream()
                .map(tournamentMapper::toResponse)
                .collect(Collectors.toList());
        stats.setRecentTournaments(recentTournaments);

        // 5. Team Performance Chart Data (Points scored vs Points conceded)
        List<ChartDataPoint> performanceChart = topTeams.stream()
                .map(t -> new ChartDataPoint(t.getTeamName(), t.getPointsScored(), t.getPointsConceded()))
                .collect(Collectors.toList());
        stats.setTeamPerformanceChart(performanceChart);

        // 6. Sport Distribution Chart Data
        Map<SportType, Long> sportCounts = tournamentRepository.findAll().stream()
                .collect(Collectors.groupingBy(Tournament::getSportType, Collectors.counting()));

        List<ChartDataPoint> sportDistribution = sportCounts.entrySet().stream()
                .map(e -> new ChartDataPoint(e.getKey().getDisplayName(), e.getValue()))
                .collect(Collectors.toList());
        stats.setSportDistributionChart(sportDistribution);

        // 7. Match Status Distribution
        List<ChartDataPoint> matchStatusChart = List.of(
                new ChartDataPoint("Completed", stats.getCompletedMatches()),
                new ChartDataPoint("Upcoming", stats.getUpcomingMatches()),
                new ChartDataPoint("Live", stats.getLiveMatches())
        );
        stats.setMatchStatusChart(matchStatusChart);

        return stats;
    }
}

