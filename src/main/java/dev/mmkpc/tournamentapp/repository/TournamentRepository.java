package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.Branch;
import dev.mmkpc.tournamentapp.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TournamentRepository extends JpaRepository<Tournament,Long> {

    boolean existsByYearAndBranch(int year, Branch branch);
}
