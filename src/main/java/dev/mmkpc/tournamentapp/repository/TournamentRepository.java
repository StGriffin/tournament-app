package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.Branch;
import dev.mmkpc.tournamentapp.model.Team;
import dev.mmkpc.tournamentapp.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament,Long> {

    boolean existsByYearAndBranch(int year, Branch branch);
}
