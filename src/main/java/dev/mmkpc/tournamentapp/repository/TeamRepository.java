package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team,Long> {
    boolean existsByName(String name);

}
