package dev.mmkpc.tournamentapp.controller;

import dev.mmkpc.tournamentapp.dto.TournamentCreationDto;
import dev.mmkpc.tournamentapp.dto.TournamentUpdateDto;
import dev.mmkpc.tournamentapp.model.Tournament;
import dev.mmkpc.tournamentapp.repository.TournamentRepository;
import dev.mmkpc.tournamentapp.service.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;
    public TournamentController(TournamentRepository tournamentRepository, TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public List<Tournament> getAllTournaments() {

        return tournamentService.getAllTournaments();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTournament(@RequestBody TournamentCreationDto request) {
        try {
            tournamentService.createTournament(request);
            return ResponseEntity.ok("Turnuva başarıyla oluşturuldu");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{tournamentId}")
    public ResponseEntity<?> deleteTournament(@PathVariable Long tournamentId) {
        try {
            tournamentService.deleteTournament(tournamentId);
            return ResponseEntity.ok("Turnuva başarıyla silindi");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping("/updateTournament")
    public ResponseEntity<?> updateTournament(@RequestBody TournamentUpdateDto tournamentUpdateDto) {

        try {
            tournamentService.updateTournament(tournamentUpdateDto);
            return ResponseEntity.ok("Turnuva başarıyla güncellendi");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }
}