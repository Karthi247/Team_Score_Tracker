package com.scoretracker.repository;

import com.scoretracker.entity.Tournament;
import com.scoretracker.enums.SportType;
import com.scoretracker.enums.TournamentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByStatus(TournamentStatus status);

    @Query("SELECT t FROM Tournament t WHERE " +
           "(:query IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(t.venue) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:sportType IS NULL OR t.sportType = :sportType)")
    Page<Tournament> searchTournaments(@Param("query") String query,
                                       @Param("status") TournamentStatus status,
                                       @Param("sportType") SportType sportType,
                                       Pageable pageable);
}
