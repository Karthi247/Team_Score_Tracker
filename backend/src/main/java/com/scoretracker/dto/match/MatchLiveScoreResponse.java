package com.scoretracker.dto.match;

import com.scoretracker.dto.score.ScoreEventResponse;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.enums.SportType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MatchLiveScoreResponse {
    private Long id;
    private Long tournamentId;
    private String tournamentName;
    private SportType sportType;
    private Long teamAId;
    private String teamAName;
    private String teamALogo;
    private Long teamBId;
    private String teamBName;
    private String teamBLogo;
    private Integer teamAScore;
    private Integer teamBScore;
    private MatchStatus status;
    private String currentPeriod;
    private Integer elapsedMinutes;
    private Long winnerId;
    private String winnerName;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private String venue;
    private List<ScoreEventResponse> scoreEvents;

    public MatchLiveScoreResponse() {
    }

    public MatchLiveScoreResponse(Long id, Long tournamentId, String tournamentName, SportType sportType,
                                  Long teamAId, String teamAName, String teamALogo,
                                  Long teamBId, String teamBName, String teamBLogo,
                                  Integer teamAScore, Integer teamBScore, MatchStatus status,
                                  String currentPeriod, Integer elapsedMinutes, Long winnerId,
                                  String winnerName, LocalDate matchDate, LocalTime matchTime,
                                  String venue, List<ScoreEventResponse> scoreEvents) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.sportType = sportType;
        this.teamAId = teamAId;
        this.teamAName = teamAName;
        this.teamALogo = teamALogo;
        this.teamBId = teamBId;
        this.teamBName = teamBName;
        this.teamBLogo = teamBLogo;
        this.teamAScore = teamAScore;
        this.teamBScore = teamBScore;
        this.status = status;
        this.currentPeriod = currentPeriod;
        this.elapsedMinutes = elapsedMinutes;
        this.winnerId = winnerId;
        this.winnerName = winnerName;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.venue = venue;
        this.scoreEvents = scoreEvents;
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

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    public Long getTeamAId() {
        return teamAId;
    }

    public void setTeamAId(Long teamAId) {
        this.teamAId = teamAId;
    }

    public String getTeamAName() {
        return teamAName;
    }

    public void setTeamAName(String teamAName) {
        this.teamAName = teamAName;
    }

    public String getTeamALogo() {
        return teamALogo;
    }

    public void setTeamALogo(String teamALogo) {
        this.teamALogo = teamALogo;
    }

    public Long getTeamBId() {
        return teamBId;
    }

    public void setTeamBId(Long teamBId) {
        this.teamBId = teamBId;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public void setTeamBName(String teamBName) {
        this.teamBName = teamBName;
    }

    public String getTeamBLogo() {
        return teamBLogo;
    }

    public void setTeamBLogo(String teamBLogo) {
        this.teamBLogo = teamBLogo;
    }

    public Integer getTeamAScore() {
        return teamAScore;
    }

    public void setTeamAScore(Integer teamAScore) {
        this.teamAScore = teamAScore;
    }

    public Integer getTeamBScore() {
        return teamBScore;
    }

    public void setTeamBScore(Integer teamBScore) {
        this.teamBScore = teamBScore;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public Integer getElapsedMinutes() {
        return elapsedMinutes;
    }

    public void setElapsedMinutes(Integer elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public LocalTime getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(LocalTime matchTime) {
        this.matchTime = matchTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public List<ScoreEventResponse> getScoreEvents() {
        return scoreEvents;
    }

    public void setScoreEvents(List<ScoreEventResponse> scoreEvents) {
        this.scoreEvents = scoreEvents;
    }
}

