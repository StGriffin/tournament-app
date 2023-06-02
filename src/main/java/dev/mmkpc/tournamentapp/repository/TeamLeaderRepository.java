package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.TeamLeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamLeaderRepository extends JpaRepository<TeamLeader,Long> {
}
