package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.Standings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandingsRepository extends JpaRepository<Standings,Long> {
}
