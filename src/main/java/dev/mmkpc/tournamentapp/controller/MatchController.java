package dev.mmkpc.tournamentapp.controller;

import dev.mmkpc.tournamentapp.dto.MatchResultDto;
import dev.mmkpc.tournamentapp.dto.MathCreationRequestDto;
import dev.mmkpc.tournamentapp.model.Match;
import dev.mmkpc.tournamentapp.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/matches")
public class MatchController {


    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMatches(@RequestBody MathCreationRequestDto mathCreationRequestDto) {
        try {
            matchService.createMatches(mathCreationRequestDto);
            return ResponseEntity.ok("Maçlar oluşturuldu.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Maçlar oluşturulamadı.");
        }
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<?> getMatchesByTournament(@PathVariable Long tournamentId) {
        try {
            List<Match> matches = matchService.getMatchesByTournament(tournamentId);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Maçlar çekilemedi: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveMatchResult(@RequestBody MatchResultDto matchResultDto) {
        try {
            matchService.saveMatchResult(matchResultDto.getMatchId(), matchResultDto.getResult());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}