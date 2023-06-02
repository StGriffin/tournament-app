package dev.mmkpc.tournamentapp.controller;

import dev.mmkpc.tournamentapp.dto.PlayerAddRequestDto;
import dev.mmkpc.tournamentapp.dto.PlayerDeleteRequest;
import dev.mmkpc.tournamentapp.dto.UserDto;
import dev.mmkpc.tournamentapp.model.Team;
import dev.mmkpc.tournamentapp.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        Team createdTeam = teamService.createTeam(team);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @PostMapping("/addPlayer")
    public ResponseEntity<?> addPlayerToTeam(@RequestBody PlayerAddRequestDto playerAddRequestDto) {
        try {
            teamService.addPlayerToTeam(playerAddRequestDto);
            return ResponseEntity.ok("Oyuncu başarıyla takıma eklendi");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/removePlayer")
    public ResponseEntity<String> removePlayerFromTeam(@RequestBody PlayerDeleteRequest playerDeleteRequest) {
        try {
            teamService.removePlayerFromTeam(playerDeleteRequest);
            return ResponseEntity.ok("Player removed from the team successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove player from the team.");
        }
    }

    @PostMapping("/assignTeamLeader")
    public ResponseEntity<String> addTeamLeaderToTeam(@PathVariable Long teamId, @RequestBody UserDto userDto) {
        try {
            teamService.addTeamLeaderToTeam(teamId, userDto);
            return ResponseEntity.ok("Team leader added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding team leader: " + e.getMessage());
        }
    }

    @DeleteMapping("/removeTeamLeader")
    public ResponseEntity<?> removeTeamLeaderFromTeam(@PathVariable Long teamId) {
        try {
            teamService.removeTeamLeaderFromTeam(teamId);
            return ResponseEntity.ok("Team leader removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing team leader: " + e.getMessage());
        }
    }

    @GetMapping("/candidateTeamLeaders")
    public ResponseEntity<?> getCandidateTeamLeaders() {
        try {
            List<UserDto> candidateTeamLeaders = teamService.getCandidateTeamLeaders();
            return ResponseEntity.ok(candidateTeamLeaders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting candidate team leaders: " + e.getMessage());
        }
    }
}

