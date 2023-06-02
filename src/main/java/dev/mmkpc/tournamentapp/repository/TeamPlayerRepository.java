package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.TeamPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayer,Long> {
}
