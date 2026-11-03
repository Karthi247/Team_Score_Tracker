package com.scoretracker.mapper;

import com.scoretracker.dto.player.PlayerResponse;
import com.scoretracker.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerResponse toResponse(Player player) {
        if (player == null) {
            return null;
        }
        Long teamId = player.getTeam() != null ? player.getTeam().getId() : null;
        String teamName = player.getTeam() != null ? player.getTeam().getName() : null;

        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getJerseyNumber(),
                player.getAge(),
                player.getPosition(),
                teamId,
                teamName,
                player.getProfileImageUrl(),
                player.getStatus(),
                player.getCreatedAt()
        );
    }
}

