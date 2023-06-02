package dev.mmkpc.tournamentapp.service;

import dev.mmkpc.tournamentapp.dto.TournamentCreationDto;
import dev.mmkpc.tournamentapp.dto.TournamentUpdateDto;
import dev.mmkpc.tournamentapp.model.Branch;
import dev.mmkpc.tournamentapp.model.Team;
import dev.mmkpc.tournamentapp.model.Tournament;
import dev.mmkpc.tournamentapp.repository.TeamRepository;
import dev.mmkpc.tournamentapp.repository.TournamentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    public TournamentService(TournamentRepository tournamentRepository, TeamRepository teamRepository) {
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
    }

    public void createTournament(TournamentCreationDto tournament) {
        int year = tournament.getYear();
        Branch branch = tournament.getBranch();
        int teamPlayerCount = tournament.getTeamPlayerCount();

        // Veritabanında branş ve yıl kombinasyonunu kontrol et
        boolean existingTournament = tournamentRepository.existsByYearAndBranch(year, branch);

        if (existingTournament) {
            throw new RuntimeException("Bu branş için zaten bir turnuva düzenlenmiş.");
        }
        Tournament newTournament = new Tournament(year,branch, Collections.emptyList(),teamPlayerCount);
        tournamentRepository.save(newTournament);
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public void deleteTournament(Long tournamentId) {
        boolean existingTournament = tournamentRepository.existsById(tournamentId);
        if(!existingTournament){
            throw new RuntimeException("Bu branş için zaten bir turnuva düzenlenmiş.");
        }
        tournamentRepository.deleteById(tournamentId);

    }

    public List<Team> getAllParticipatingTeams() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        List<Team> participatingTeams = new ArrayList<>();

        for (Tournament tournament : tournaments) {
            participatingTeams.addAll(tournament.getTeams());
        }

        return participatingTeams;
    }

    public void updateTournament(TournamentUpdateDto tournamentUpdateDto) {
        Tournament tournament = tournamentRepository.findById(tournamentUpdateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Turnuva bulunamadı"));

        tournament.setYear(tournamentUpdateDto.getYear());
        tournament.setBranch(tournamentUpdateDto.getBranch());
        tournament.setTeamPlayerCount(tournamentUpdateDto.getTeamPlayerCount());

        List<Long> teamIds = tournamentUpdateDto.getTeams();
        List<Team> teams = teamRepository.findAllById(teamIds);
        tournament.setTeams(teams);

        tournamentRepository.save(tournament);
    }
}