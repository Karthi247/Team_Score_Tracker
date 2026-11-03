package com.scoretracker.dto.dashboard;

import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.dto.team.TeamStatsResponse;
import com.scoretracker.dto.tournament.TournamentResponse;

import java.util.List;

public class DashboardStatsResponse {
    private long totalTeams;
    private long totalPlayers;
    private long totalTournaments;
    private long totalMatches;
    private long completedMatches;
    private long upcomingMatches;
    private long liveMatches;

    private List<MatchResponse> recentMatches;
    private List<MatchResponse> upcomingMatchesList;
    private List<MatchResponse> liveMatchesList;
    private List<TeamStatsResponse> topTeams;
    private List<TournamentResponse> recentTournaments;
    private List<ChartDataPoint> teamPerformanceChart;
    private List<ChartDataPoint> sportDistributionChart;
    private List<ChartDataPoint> matchStatusChart;

    public DashboardStatsResponse() {
    }

    public long getTotalTeams() {
        return totalTeams;
    }

    public void setTotalTeams(long totalTeams) {
        this.totalTeams = totalTeams;
    }

    public long getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(long totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public long getTotalTournaments() {
        return totalTournaments;
    }

    public void setTotalTournaments(long totalTournaments) {
        this.totalTournaments = totalTournaments;
    }

    public long getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(long totalMatches) {
        this.totalMatches = totalMatches;
    }

    public long getCompletedMatches() {
        return completedMatches;
    }

    public void setCompletedMatches(long completedMatches) {
        this.completedMatches = completedMatches;
    }

    public long getUpcomingMatches() {
        return upcomingMatches;
    }

    public void setUpcomingMatches(long upcomingMatches) {
        this.upcomingMatches = upcomingMatches;
    }

    public long getLiveMatches() {
        return liveMatches;
    }

    public void setLiveMatches(long liveMatches) {
        this.liveMatches = liveMatches;
    }

    public List<MatchResponse> getRecentMatches() {
        return recentMatches;
    }

    public void setRecentMatches(List<MatchResponse> recentMatches) {
        this.recentMatches = recentMatches;
    }

    public List<MatchResponse> getUpcomingMatchesList() {
        return upcomingMatchesList;
    }

    public void setUpcomingMatchesList(List<MatchResponse> upcomingMatchesList) {
        this.upcomingMatchesList = upcomingMatchesList;
    }

    public List<MatchResponse> getLiveMatchesList() {
        return liveMatchesList;
    }

    public void setLiveMatchesList(List<MatchResponse> liveMatchesList) {
        this.liveMatchesList = liveMatchesList;
    }

    public List<TeamStatsResponse> getTopTeams() {
        return topTeams;
    }

    public void setTopTeams(List<TeamStatsResponse> topTeams) {
        this.topTeams = topTeams;
    }

    public List<TournamentResponse> getRecentTournaments() {
        return recentTournaments;
    }

    public void setRecentTournaments(List<TournamentResponse> recentTournaments) {
        this.recentTournaments = recentTournaments;
    }

    public List<ChartDataPoint> getTeamPerformanceChart() {
        return teamPerformanceChart;
    }

    public void setTeamPerformanceChart(List<ChartDataPoint> teamPerformanceChart) {
        this.teamPerformanceChart = teamPerformanceChart;
    }

    public List<ChartDataPoint> getSportDistributionChart() {
        return sportDistributionChart;
    }

    public void setSportDistributionChart(List<ChartDataPoint> sportDistributionChart) {
        this.sportDistributionChart = sportDistributionChart;
    }

    public List<ChartDataPoint> getMatchStatusChart() {
        return matchStatusChart;
    }

    public void setMatchStatusChart(List<ChartDataPoint> matchStatusChart) {
        this.matchStatusChart = matchStatusChart;
    }
}

