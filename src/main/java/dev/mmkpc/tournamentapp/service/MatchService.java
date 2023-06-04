package dev.mmkpc.tournamentapp.service;

import dev.mmkpc.tournamentapp.model.Match;
import dev.mmkpc.tournamentapp.model.Team;
import dev.mmkpc.tournamentapp.model.Tournament;
import dev.mmkpc.tournamentapp.repository.MatchRepository;
import dev.mmkpc.tournamentapp.repository.TeamRepository;
import dev.mmkpc.tournamentapp.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository, TournamentRepository tournamentRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public void createMatches(List<Long> teamIds) {
        // Tarihleri hesapla ve maçları oluştur
        LocalDate matchDate = LocalDate.now();
        for (int i = 0; i < teamIds.size(); i++) {
            for (int j = 0; j < teamIds.size(); j++) {
                if(i!=j) {
                    Match match = new Match();
                    Optional<Team> homeTeam = teamRepository.findById(teamIds.get(i));
                    Optional<Team> awayTeam = teamRepository.findById(teamIds.get(j));
                    match.setHomeTeam(homeTeam.get());
                    match.setAwayTeam(awayTeam.get());
                    match.setMatchDate(matchDate);
                    matchRepository.save(match);
                    matchDate = matchDate.plusDays(1);
                }
            }
        }
    }

    public List<Match> getMatchesByTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        List<Team> teams = tournament.getTeams();
        return matchRepository.findByHomeTeamInOrAwayTeamIn(teams, teams);
    }
}
