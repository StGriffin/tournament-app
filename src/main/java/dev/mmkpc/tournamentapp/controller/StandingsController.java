package dev.mmkpc.tournamentapp.controller;

import dev.mmkpc.tournamentapp.model.Standings;
import dev.mmkpc.tournamentapp.repository.StandingsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/standings")
public class StandingsController {

    private final StandingsRepository standingsRepository;

    public StandingsController(StandingsRepository standingsRepository) {
        this.standingsRepository = standingsRepository;
    }

    @GetMapping
    public Optional<Standings> getStandingsByTournamentId(@RequestParam Long tournamentId) {
        return standingsRepository.findById(tournamentId);
    }
}