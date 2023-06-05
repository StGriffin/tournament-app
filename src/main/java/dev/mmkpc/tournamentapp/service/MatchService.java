package dev.mmkpc.tournamentapp.service;

import dev.mmkpc.tournamentapp.dto.MathCreationRequestDto;
import dev.mmkpc.tournamentapp.model.Match;
import dev.mmkpc.tournamentapp.model.Standings;
import dev.mmkpc.tournamentapp.model.Team;
import dev.mmkpc.tournamentapp.model.Tournament;
import dev.mmkpc.tournamentapp.repository.MatchRepository;
import dev.mmkpc.tournamentapp.repository.StandingsRepository;
import dev.mmkpc.tournamentapp.repository.TeamRepository;
import dev.mmkpc.tournamentapp.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final StandingsRepository standingsRepository;
    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository, TournamentRepository tournamentRepository, StandingsRepository standingsRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
        this.standingsRepository = standingsRepository;
    }

    public void createMatches(MathCreationRequestDto mathCreationRequestDto) {
        Long tournamentId = mathCreationRequestDto.getTournamentId();
        List<Long> teamIds = mathCreationRequestDto.getTeamIds();
        List<String> matches = new ArrayList<>();

        for (int i = 0; i < teamIds.size(); i++) {
            for (int j = 0 ; j < teamIds.size(); j++) {
                if(i!=j){
                matches.add(teamIds.get(i) + "," + teamIds.get(j));
                }
            }
        }
        //Karşılaşmaları karıştır
        Collections.shuffle(matches);
        // Tarihleri hesapla ve maçları oluştur
        LocalDate matchDate = LocalDate.now();
        for(String matchPair : matches) {
            String[] tIds = matchPair.split(",");
            Long homeTeamId= Long.parseLong(tIds[0]);
            Long awayTeamId= Long.parseLong(tIds[1]);
            Match match = new Match();
            Optional<Team> homeTeam = teamRepository.findById(homeTeamId);
            Optional<Team> awayTeam = teamRepository.findById(awayTeamId);
            match.setHomeTeam(homeTeam.get());
            match.setAwayTeam(awayTeam.get());
            match.setMatchDate(matchDate);
            matchRepository.save(match);
            matchDate = matchDate.plusDays(1);
        }
        //Turnuvayı başlat ve puan durumları tablosuna ekleme yap
        Optional<Tournament> currentTournament = tournamentRepository.findById(tournamentId);
        if(currentTournament.isPresent()){
            currentTournament.get().setActive(true);
            tournamentRepository.save(currentTournament.get());
            for(int i=0 ; i<teamIds.size();i++){
                Standings standings = new Standings();
                Team selectedTeam = teamRepository.findById(teamIds.get(i)).get();
                standings.setTournament(currentTournament.get());
                standings.setTeam(selectedTeam);
                standingsRepository.save(standings);

            }
        }

    }

    public List<Match> getMatchesByTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        List<Team> teams = tournament.getTeams();
        return matchRepository.findByHomeTeamInOrAwayTeamIn(teams, teams);
    }

    public void saveMatchResult(Long matchId, String result) {
       Optional<Match> match = matchRepository.findById(matchId);
       if(match.isPresent()){
           match.get().setScore(result);
       }

        Optional<Standings> homeTeamStanding = standingsRepository.findByTeamId(match.get().getHomeTeam().getId());
        Optional<Standings> awayTeamStanding =  standingsRepository.findByTeamId(match.get().getAwayTeam().getId());

        if(awayTeamStanding.isPresent() && homeTeamStanding.isPresent()){
            Standings homeTeam = homeTeamStanding.get();
            Standings awayTeam = awayTeamStanding.get();
            //----Skora Göre --------
            String[] score = result.split("-");
            int homeTeamScore = Integer.parseInt(score[0]);
            int awayTeamScore = Integer.parseInt(score[1]);

            if(homeTeamScore>awayTeamScore){
                // -------KAZANAN TAKIM------------//
                homeTeam.setMatchesPlayed(homeTeam.getMatchesPlayed()+1); // Oynanan maçı 1 artır.
                homeTeam.setWins(homeTeam.getWins()+1); // Kazanılan maçı 1 artır
                homeTeam.setGoalsScored(homeTeam.getGoalsScored()+homeTeamScore);// Atılan golü artır
                homeTeam.setGoalsConceded(homeTeam.getGoalsConceded()+awayTeamScore); // Yenilen golü artır
                homeTeam.setGoalDifference(homeTeam.getGoalsScored()-homeTeam.getGoalsConceded()); // Averajı belirle
                homeTeam.setPoints(homeTeam.getPoints()+3); // Kazanan takıma 3 puan ver.

                //------KAYBEDEN TAKIM ----------//
                awayTeam.setMatchesPlayed(awayTeam.getMatchesPlayed()+1); // Oynanan maçı 1 artır.
                awayTeam.setLosses(awayTeam.getLosses()+1); // Kazanılan maçı 1 artır
                awayTeam.setGoalsScored(awayTeam.getGoalsScored()+awayTeamScore);// Atılan golü artır
                awayTeam.setGoalsConceded(awayTeam.getGoalsConceded()+homeTeamScore); // Yenilen golü artır
                awayTeam.setGoalDifference(awayTeam.getGoalsScored()-awayTeam.getGoalsConceded()); // Averajı belirle
                //Puan yok.
            }

            else if(homeTeamScore==awayTeamScore){
                // -------1. TAKIM------------//
                homeTeam.setMatchesPlayed(homeTeam.getMatchesPlayed()+1); // Oynanan maçı 1 artır.
                homeTeam.setDraws(homeTeam.getDraws()+1); // Beraberliği 1 artır.
                homeTeam.setGoalsScored(homeTeam.getGoalsScored()+homeTeamScore);// Atılan golü artır
                homeTeam.setGoalsConceded(homeTeam.getGoalsConceded()+awayTeamScore); // Yenilen golü artır
                homeTeam.setGoalDifference(homeTeam.getGoalsScored()-homeTeam.getGoalsConceded()); // Averajı belirle
                homeTeam.setPoints(homeTeam.getPoints()+1); // Beraberlikte takıma 1 puan ver.

                //------2. TAKIM ----------//
                awayTeam.setMatchesPlayed(awayTeam.getMatchesPlayed()+1); // Oynanan maçı 1 artır.
                awayTeam.setDraws(awayTeam.getDraws()+1); // Beraberliği 1 artır
                awayTeam.setGoalsScored(awayTeam.getGoalsScored()+awayTeamScore);// Atılan golü artır
                awayTeam.setGoalsConceded(awayTeam.getGoalsConceded()+homeTeamScore); // Yenilen golü artır
                awayTeam.setGoalDifference(awayTeam.getGoalsScored()-awayTeam.getGoalsConceded()); // Averajı belirle
                awayTeam.setPoints(awayTeam.getPoints()+1); // Beraberlikte 1 puam ver.

            }
            else{
                // -------KAZANAN TAKIM------------//
                awayTeam.setMatchesPlayed(awayTeam.getMatchesPlayed()+1); // Oynanan maçı 1 artır.
                awayTeam.setWins(awayTeam.getWins()+1); // Kazanılan maçı 1 artır
                awayTeam.setGoalsScored(awayTeam.getGoalsScored()+awayTeamScore);// Atılan golü artır
                awayTeam.setGoalsConceded(awayTeam.getGoalsConceded()+homeTeamScore); // Yenilen golü artır
                awayTeam.setGoalDifference(awayTeam.getGoalsScored()-awayTeam.getGoalsConceded()); // Averajı belirle
                awayTeam.setPoints(awayTeam.getPoints()+3); // Kazanan takıma 3 puan ver.

                //------KAYBEDEN TAKIM ----------//
                homeTeam.setMatchesPlayed(homeTeam.getMatchesPlayed()+1); // Oynanan maçı 1 artır.
                homeTeam.setLosses(homeTeam.getLosses()+1); // Kazanılan maçı 1 artır
                homeTeam.setGoalsScored(homeTeam.getGoalsScored()+homeTeamScore);// Atılan golü artır
                homeTeam.setGoalsConceded(homeTeam.getGoalsConceded()+homeTeamScore); // Yenilen golü artır
                homeTeam.setGoalDifference(homeTeam.getGoalsScored()-homeTeam.getGoalsConceded()); // Averajı belirle
                //Puan yok.
            }

            standingsRepository.save(homeTeam);
            standingsRepository.save(awayTeam);
        }

       matchRepository.save(match.get());
    }
}
