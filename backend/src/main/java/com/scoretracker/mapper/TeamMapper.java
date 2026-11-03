package com.scoretracker.mapper;

import com.scoretracker.dto.team.TeamResponse;
import com.scoretracker.entity.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public TeamResponse toResponse(Team team) {
        if (team == null) {
            return null;
        }
        int playerCount = team.getPlayers() != null ? team.getPlayers().size() : 0;
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getLogoUrl(),
                team.getDescription(),
                team.getCaptainName(),
                team.getStatus(),
                playerCount,
                team.getCreatedAt(),
                team.getUpdatedAt()
        );
    }
}

