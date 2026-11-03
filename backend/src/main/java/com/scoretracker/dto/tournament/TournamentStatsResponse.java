package com.scoretracker.dto.tournament;

public class TournamentStatsResponse {
    private Long tournamentId;
    private String tournamentName;
    private int totalMatches;
    private int completedMatches;
    private int liveMatches;
    private int upcomingMatches;
    private int totalPointsScored;
    private double averagePointsPerMatch;
    private String leadingTeamName;
    private Long leadingTeamId;

    public TournamentStatsResponse() {
    }

    public TournamentStatsResponse(Long tournamentId, String tournamentName, int totalMatches,
                                   int completedMatches, int liveMatches, int upcomingMatches,
                                   int totalPointsScored, double averagePointsPerMatch,
                                   String leadingTeamName, Long leadingTeamId) {
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.totalMatches = totalMatches;
        this.completedMatches = completedMatches;
        this.liveMatches = liveMatches;
        this.upcomingMatches = upcomingMatches;
        this.totalPointsScored = totalPointsScored;
        this.averagePointsPerMatch = averagePointsPerMatch;
        this.leadingTeamName = leadingTeamName;
        this.leadingTeamId = leadingTeamId;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getCompletedMatches() {
        return completedMatches;
    }

    public void setCompletedMatches(int completedMatches) {
        this.completedMatches = completedMatches;
    }

    public int getLiveMatches() {
        return liveMatches;
    }

    public void setLiveMatches(int liveMatches) {
        this.liveMatches = liveMatches;
    }

    public int getUpcomingMatches() {
        return upcomingMatches;
    }

    public void setUpcomingMatches(int upcomingMatches) {
        this.upcomingMatches = upcomingMatches;
    }

    public int getTotalPointsScored() {
        return totalPointsScored;
    }

    public void setTotalPointsScored(int totalPointsScored) {
        this.totalPointsScored = totalPointsScored;
    }

    public double getAveragePointsPerMatch() {
        return averagePointsPerMatch;
    }

    public void setAveragePointsPerMatch(double averagePointsPerMatch) {
        this.averagePointsPerMatch = averagePointsPerMatch;
    }

    public String getLeadingTeamName() {
        return leadingTeamName;
    }

    public void setLeadingTeamName(String leadingTeamName) {
        this.leadingTeamName = leadingTeamName;
    }

    public Long getLeadingTeamId() {
        return leadingTeamId;
    }

    public void setLeadingTeamId(Long leadingTeamId) {
        this.leadingTeamId = leadingTeamId;
    }
}

