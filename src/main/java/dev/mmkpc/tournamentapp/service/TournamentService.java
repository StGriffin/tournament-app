package dev.mmkpc.tournamentapp.service;

import dev.mmkpc.tournamentapp.dto.TournamentCreationDto;
import dev.mmkpc.tournamentapp.model.Branch;
import dev.mmkpc.tournamentapp.model.Tournament;
import dev.mmkpc.tournamentapp.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
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
}