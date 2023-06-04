package dev.mmkpc.tournamentapp.controller;

import dev.mmkpc.tournamentapp.dto.PlayerAddRequestDto;
import dev.mmkpc.tournamentapp.dto.PlayerDeleteRequest;
import dev.mmkpc.tournamentapp.dto.TeamUpdateDto;
import dev.mmkpc.tournamentapp.dto.UserDto;
import dev.mmkpc.tournamentapp.model.Team;
import dev.mmkpc.tournamentapp.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/createTeam")
    public ResponseEntity<String> createTeam(@RequestBody Map<String, Object> requestData) {
        String teamName = (String) requestData.get("teamName");

        Long teamLeadId = Long.parseLong(String.valueOf(requestData.get("teamLeadId")));
        try {
            teamService.createTeam(teamName,teamLeadId);
            return ResponseEntity.ok("Takım başarıyla oluşturuldu");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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

    @GetMapping("/availableTeams")
    public ResponseEntity<List<Team>> getAllAvailableTeams() {
        List<Team> teams = teamService.getAllAvailableTeams();
        return ResponseEntity.ok(teams);
    }


    @GetMapping("/availableUsers")
    public ResponseEntity<List<UserDto>> getAllAvailableUsers(){
        List<UserDto> availableUsers = teamService.getAllAvailableUsers();
        return ResponseEntity.ok(availableUsers);
    }

    @PutMapping("/updateTeam")
    public ResponseEntity<String> updateTeamInfo(
           @RequestBody TeamUpdateDto teamUpdateDto
    ) {
        try {
            teamService.updateTeamInfo(teamUpdateDto);
            return ResponseEntity.ok("Takım başarıyla güncellendi");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Takım güncellenmesinde bir hata oluştu: " + e.getMessage());
        }
    }
}

