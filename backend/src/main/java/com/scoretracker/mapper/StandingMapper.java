package com.scoretracker.mapper;

import com.scoretracker.dto.standing.StandingResponse;
import com.scoretracker.entity.TeamStanding;
import org.springframework.stereotype.Component;

@Component
public class StandingMapper {

    public StandingResponse toResponse(TeamStanding standing) {
        if (standing == null) {
            return null;
        }

        Long tournamentId = standing.getTournament() != null ? standing.getTournament().getId() : null;
        Long teamId = standing.getTeam() != null ? standing.getTeam().getId() : null;
        String teamName = standing.getTeam() != null ? standing.getTeam().getName() : "Team";
        String teamLogo = standing.getTeam() != null ? standing.getTeam().getLogoUrl() : null;

        return new StandingResponse(
                standing.getId(),
                tournamentId,
                teamId,
                teamName,
                teamLogo,
                standing.getMatchesPlayed(),
                standing.getWins(),
                standing.getDraws(),
                standing.getLosses(),
                standing.getPointsScored(),
                standing.getPointsConceded(),
                standing.getScoreDifference(),
                standing.getTournamentPoints(),
                standing.getPositionRank()
        );
    }
}

