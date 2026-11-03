package com.scoretracker.repository;

import com.scoretracker.entity.Player;
import com.scoretracker.enums.PlayerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeamId(Long teamId);
    
    boolean existsByTeamIdAndJerseyNumber(Long teamId, Integer jerseyNumber);
    
    boolean existsByTeamIdAndJerseyNumberAndIdNot(Long teamId, Integer jerseyNumber, Long id);

    @Query("SELECT p FROM Player p WHERE " +
           "(:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.position) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:teamId IS NULL OR p.team.id = :teamId) AND " +
           "(:status IS NULL OR p.status = :status)")
    Page<Player> searchPlayers(@Param("query") String query,
                               @Param("teamId") Long teamId,
                               @Param("status") PlayerStatus status,
                               Pageable pageable);
}
