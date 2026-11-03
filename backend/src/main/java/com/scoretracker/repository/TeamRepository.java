package com.scoretracker.repository;

import com.scoretracker.entity.Team;
import com.scoretracker.enums.TeamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByNameIgnoreCase(String name);
    Boolean existsByNameIgnoreCase(String name);
    List<Team> findByStatus(TeamStatus status);

    @Query("SELECT t FROM Team t WHERE " +
           "(:query IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(t.captainName) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:status IS NULL OR t.status = :status)")
    Page<Team> searchTeams(@Param("query") String query, @Param("status") TeamStatus status, Pageable pageable);
}
