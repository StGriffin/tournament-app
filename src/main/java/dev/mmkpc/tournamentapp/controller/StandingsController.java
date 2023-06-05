package dev.mmkpc.tournamentapp.controller;

import dev.mmkpc.tournamentapp.model.Standings;
import dev.mmkpc.tournamentapp.repository.StandingsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/standings")
public class StandingsController {

    private final StandingsRepository standingsRepository;

    public StandingsController(StandingsRepository standingsRepository) {
        this.standingsRepository = standingsRepository;
    }

    @GetMapping
    public List<Standings> getStandingsByTournamentId(@RequestParam("tournamentId") Long tournamentId) {
        return standingsRepository.findByTournamentId(tournamentId);
    }
}