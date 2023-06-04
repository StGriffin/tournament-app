package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.Match;
import dev.mmkpc.tournamentapp.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Long> {
    List<Match> findByHomeTeamInOrAwayTeamIn(List<Team> teams, List<Team> teams1);
}
