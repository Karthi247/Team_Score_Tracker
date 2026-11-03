package com.scoretracker.dto.standing;

public class StandingResponse {
    private Long id;
    private Long tournamentId;
    private Long teamId;
    private String teamName;
    private String teamLogo;
    private Integer matchesPlayed;
    private Integer wins;
    private Integer draws;
    private Integer losses;
    private Integer pointsScored;
    private Integer pointsConceded;
    private Integer scoreDifference;
    private Integer tournamentPoints;
    private Integer positionRank;

    public StandingResponse() {
    }

    public StandingResponse(Long id, Long tournamentId, Long teamId, String teamName,
                            String teamLogo, Integer matchesPlayed, Integer wins,
                            Integer draws, Integer losses, Integer pointsScored,
                            Integer pointsConceded, Integer scoreDifference,
                            Integer tournamentPoints, Integer positionRank) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.matchesPlayed = matchesPlayed;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.pointsScored = pointsScored;
        this.pointsConceded = pointsConceded;
        this.scoreDifference = scoreDifference;
        this.tournamentPoints = tournamentPoints;
        this.positionRank = positionRank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
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

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getPointsScored() {
        return pointsScored;
    }

    public void setPointsScored(Integer pointsScored) {
        this.pointsScored = pointsScored;
    }

    public Integer getPointsConceded() {
        return pointsConceded;
    }

    public void setPointsConceded(Integer pointsConceded) {
        this.pointsConceded = pointsConceded;
    }

    public Integer getScoreDifference() {
        return scoreDifference;
    }

    public void setScoreDifference(Integer scoreDifference) {
        this.scoreDifference = scoreDifference;
    }

    public Integer getTournamentPoints() {
        return tournamentPoints;
    }

    public void setTournamentPoints(Integer tournamentPoints) {
        this.tournamentPoints = tournamentPoints;
    }

    public Integer getPositionRank() {
        return positionRank;
    }

    public void setPositionRank(Integer positionRank) {
        this.positionRank = positionRank;
    }
}

