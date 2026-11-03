package com.scoretracker.dto.team;

public class TeamStatsResponse {
    private Long teamId;
    private String teamName;
    private String logoUrl;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int draws;
    private double winRate;
    private int pointsScored;
    private int pointsConceded;
    private int tournamentsEntered;

    public TeamStatsResponse() {
    }

    public TeamStatsResponse(Long teamId, String teamName, String logoUrl, int matchesPlayed, int wins,
                             int losses, int draws, double winRate, int pointsScored,
                             int pointsConceded, int tournamentsEntered) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.logoUrl = logoUrl;
        this.matchesPlayed = matchesPlayed;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.winRate = winRate;
        this.pointsScored = pointsScored;
        this.pointsConceded = pointsConceded;
        this.tournamentsEntered = tournamentsEntered;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public int getPointsScored() {
        return pointsScored;
    }

    public void setPointsScored(int pointsScored) {
        this.pointsScored = pointsScored;
    }

    public int getPointsConceded() {
        return pointsConceded;
    }

    public void setPointsConceded(int pointsConceded) {
        this.pointsConceded = pointsConceded;
    }

    public int getTournamentsEntered() {
        return tournamentsEntered;
    }

    public void setTournamentsEntered(int tournamentsEntered) {
        this.tournamentsEntered = tournamentsEntered;
    }
}

