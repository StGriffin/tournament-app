package dev.mmkpc.tournamentapp.service;

import dev.mmkpc.tournamentapp.dto.TeamUpdateDto;
import dev.mmkpc.tournamentapp.dto.UserDto;
import dev.mmkpc.tournamentapp.model.*;

import dev.mmkpc.tournamentapp.repository.TeamLeaderRepository;
import dev.mmkpc.tournamentapp.repository.TeamPlayerRepository;
import dev.mmkpc.tournamentapp.repository.TeamRepository;
import dev.mmkpc.tournamentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public void createTeam(String teamName , Long teamLeadId) {

        boolean teamExist = teamRepository.existsByName(teamName);

        if(!teamExist) {
            Team newTeam = new Team();
            newTeam.setName(teamName);

            if (teamLeadId != null) {
                Optional<User> user = userRepository.findById(teamLeadId);
                if (user.isPresent()) {
                    if (user.get().getRole() != Role.SISTEM_YONETICISI) {
                        user.get().setRole(Role.TAKIM_SORUMLUSU);
                    }
                    userRepository.save(user.get());
                    TeamLeader newTeamLeader = new TeamLeader();
                    newTeamLeader.setUser(user.get());
                    teamLeaderRepository.save(newTeamLeader);
                    newTeam.setTeamLeader(newTeamLeader);
                }
            }

            teamRepository.save(newTeam);

        }
        else{
            throw new IllegalArgumentException("Aynı ada sahip başka bir takım zaten var: " + teamName);
        }
    }


    public List<UserDto> getAllAvailableUsers() {
        List<TeamPlayer> teamPlayers = teamPlayerRepository.findAll();
        List<Long> userIds = teamPlayers.stream()
                .map(TeamPlayer::getUser)
                .map(User::getId)
                .collect(Collectors.toList());

        List<User> availableUsers;
        if(userIds.size()==0){
            availableUsers=userRepository.findAll();
        }

        else{
            availableUsers = userRepository.findAllByIdNotIn(userIds);
        }

        List<UserDto> userDTOs = mapUserListToDTO(availableUsers);
        return userDTOs;
    }

    private List<UserDto> mapUserListToDTO(List<User> users) {
        List<UserDto> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDto userDTO = new UserDto(user.getId(), user.getUserName(), user.getFullName(), user.getAge());
            userDTOs.add(userDTO);
        }
        return userDTOs;
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

    public void updateTeamInfo(TeamUpdateDto teamUpdateDto) {

        Long teamId = teamUpdateDto.getTeamId();
        Team team = teamRepository.findById(teamUpdateDto.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Takım bulunamadı: " + teamId));
        String teamName = teamUpdateDto.getTeamName();
        Long teamLeadId = teamUpdateDto.getTeamLeadUserId();
        List<Long> playerIds = teamUpdateDto.getPlayerIds();



        team.setName((teamName));
        if(teamLeadId!=null) {
            Optional<User> userTeamLead = userRepository.findById(teamLeadId);
            TeamLeader newTeamLead = new TeamLeader();
            if (userTeamLead.isPresent()) {
                if (userTeamLead.get().getRole() != Role.SISTEM_YONETICISI) {
                    userTeamLead.get().setRole(Role.TAKIM_SORUMLUSU);
                }

                newTeamLead.setUser(userTeamLead.get());
                teamLeaderRepository.save(newTeamLead);
                team.setTeamLeader(newTeamLead);
                userRepository.save(userTeamLead.get());
            }
        }
        if(!playerIds.isEmpty()) {
            List<TeamPlayer> teamPlayers = new ArrayList<>();
            for (Long playerId : playerIds) {
                Optional<User> user = userRepository.findById(playerId);
                if (user.isPresent()) {
                    TeamPlayer teamPlayer = new TeamPlayer();
                    teamPlayer.setUser(user.get());
                    teamPlayers.add(teamPlayer);
                    teamPlayerRepository.save(teamPlayer);
                }
            }
            team.setPlayers(teamPlayers);
        }

        teamRepository.save(team);
    }
}


