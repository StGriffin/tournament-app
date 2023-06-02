package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
