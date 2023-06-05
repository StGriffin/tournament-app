package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.Standings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StandingsRepository extends JpaRepository<Standings,Long> {

    List<Standings> findByTournamentId(Long tournamentId);

    Optional<Standings> findByTeamId(Long teamId);

}
