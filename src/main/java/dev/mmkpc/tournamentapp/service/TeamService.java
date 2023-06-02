package dev.mmkpc.tournamentapp.service;

import dev.mmkpc.tournamentapp.dto.PlayerAddRequestDto;
import dev.mmkpc.tournamentapp.dto.PlayerDeleteRequest;
import dev.mmkpc.tournamentapp.dto.UserDto;
import dev.mmkpc.tournamentapp.model.*;

import dev.mmkpc.tournamentapp.repository.TeamLeaderRepository;
import dev.mmkpc.tournamentapp.repository.TeamPlayerRepository;
import dev.mmkpc.tournamentapp.repository.TeamRepository;
import dev.mmkpc.tournamentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamPlayerRepository teamPlayerRepository;

    private final TeamLeaderRepository teamLeaderRepository;
    private final UserRepository userRepository;

    private final TournamentService tournamentService;


    @Autowired
    public TeamService(TeamRepository teamRepository, TeamPlayerRepository teamPlayerRepository, TeamLeaderRepository teamLeaderRepository, UserRepository userRepository, TournamentService tournamentService) {
        this.teamRepository = teamRepository;
        this.teamPlayerRepository = teamPlayerRepository;
        this.teamLeaderRepository = teamLeaderRepository;
        this.userRepository = userRepository;
        this.tournamentService = tournamentService;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }


    public List<Team> getAllAvailableTeams() {
        List<Team> allTeams = teamRepository.findAll();
        List<Team> participatingTeams = tournamentService.getAllParticipatingTeams();

        // Turnuvaya katılan takımları filtrele
        allTeams.removeAll(participatingTeams);

        return allTeams;
    }

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    public void addPlayerToTeam(PlayerAddRequestDto playerAddRequestDto) {
        Team team = teamRepository.findById(playerAddRequestDto.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found"));
        List<TeamPlayer> players = team.getPlayers();

        // Check if adding the player violates the age limit
        if (getUserAge(playerAddRequestDto.getUserId()) < 30 && countPlayersBelowAge(players) >= 3) {
            throw new RuntimeException("Cannot add more than three players below the age of 30.");
        }

        // Create a new TeamPlayer entity
        TeamPlayer newPlayer = new TeamPlayer();
        newPlayer.setUser(userRepository.findById(playerAddRequestDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
        newPlayer.setTeam(team);
        newPlayer.setJerseyNumber(playerAddRequestDto.getJerseyNumber());

        // Save the new player to the database
        teamPlayerRepository.save(newPlayer);

        // Add the new player to the team
        players.add(newPlayer);
        team.setPlayers(players);

        teamRepository.save(team);
    }

    public void removePlayerFromTeam(PlayerDeleteRequest playerDeleteRequest) {
        Team team = teamRepository.findById(playerDeleteRequest.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        List<TeamPlayer> players = team.getPlayers();
        players.removeIf(player -> player.getId().equals(playerDeleteRequest.getPlayerId()));

        team.setPlayers(players);

        // Delete the TeamPlayer entity
        teamPlayerRepository.deleteById(playerDeleteRequest.getPlayerId());

        teamRepository.save(team);
    }

    // Helper method to get the age of a user
    private int getUserAge(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getAge();
    }

    // Helper method to count the number of players below the age of 30
    private int countPlayersBelowAge(List<TeamPlayer> players) {
        int count = 0;
        for (TeamPlayer player : players) {
            if (getUserAge(player.getUser().getId()) < 30) {
                count++;
            }
        }
        return count;
    }

    public ResponseEntity<List<UserDto>> getAllAvailableUsers() {
        List<TeamPlayer> teamPlayers = teamPlayerRepository.findAll();
        List<Long> userIds = teamPlayers.stream()
                .map(TeamPlayer::getUser)
                .map(User::getId)
                .collect(Collectors.toList());

        List<User> availableUsers = userRepository.findAllByIdNotIn(userIds);
        List<UserDto> userDTOs = mapUserListToDTO(availableUsers);
        return ResponseEntity.ok(userDTOs);
    }

    private List<UserDto> mapUserListToDTO(List<User> users) {
        List<UserDto> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDto userDTO = new UserDto(user.getId(), user.getUserName(), user.getFullName(), user.getAge());
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    public void addTeamLeaderToTeam(Long teamId, UserDto userDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TeamLeader teamLeader = new TeamLeader();
        teamLeader.setUser(user);
        teamLeader.setTeam(team);

        // User'ın rolünün güncellenmesi
        if (user.getRole() != Role.SISTEM_YONETICISI) {
            user.setRole(Role.TAKIM_SORUMLUSU);
        }

        // TeamLeader'ın kaydedilmesi
        teamLeaderRepository.save(teamLeader);
        userRepository.save(user);

        // Takımdaki TeamLeader'ın güncellenmesi
        team.setTeamLeader(teamLeader);
        teamRepository.save(team);
    }

    public void removeTeamLeaderFromTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamLeader teamLeader = team.getTeamLeader();
        if (teamLeader != null) {
            User user = teamLeader.getUser();
            if (user.getRole() != Role.SISTEM_YONETICISI) {
                user.setRole(Role.NORMAL);
            }
            team.setTeamLeader(null);
            userRepository.save(user);
        }

        teamRepository.save(team);
    }

    public List<UserDto> getCandidateTeamLeaders() {
        List<Team> teams = teamRepository.findAll();
        List<TeamLeader> teamLeaders = teamLeaderRepository.findAll();
        List<User> existingTeamLeaders = teamLeaders.stream()
                .map(TeamLeader::getUser)
                .collect(Collectors.toList());

        List<User> candidateTeamLeaders = userRepository.findAll().stream()
                .filter(user -> !existingTeamLeaders.contains(user) && user.getRole() != Role.SISTEM_YONETICISI)
                .collect(Collectors.toList());

        return candidateTeamLeaders.stream()
                .map(user -> new UserDto(user.getId(),user.getUserName(), user.getFullName(), user.getAge()))
                .collect(Collectors.toList());
    }
}


