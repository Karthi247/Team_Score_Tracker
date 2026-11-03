package com.scoretracker.mapper;

import com.scoretracker.dto.tournament.TournamentResponse;
import com.scoretracker.entity.Tournament;
import org.springframework.stereotype.Component;

@Component
public class TournamentMapper {

    public TournamentResponse toResponse(Tournament tournament) {
        if (tournament == null) {
            return null;
        }
        int registeredTeamsCount = tournament.getTournamentTeams() != null ? tournament.getTournamentTeams().size() : 0;

        return new TournamentResponse(
                tournament.getId(),
                tournament.getName(),
                tournament.getSportType(),
                tournament.getDescription(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                tournament.getVenue(),
                tournament.getMaxTeams(),
                registeredTeamsCount,
                tournament.getStatus(),
                tournament.getFormat(),
                tournament.getWinPoints(),
                tournament.getDrawPoints(),
                tournament.getLossPoints(),
                tournament.getCreatedAt()
        );
    }
}

