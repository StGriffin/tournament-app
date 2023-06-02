package dev.mmkpc.tournamentapp.dto;

import dev.mmkpc.tournamentapp.model.Branch;
import dev.mmkpc.tournamentapp.model.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentInfoDto {

    private Long id;
    private int year;
    private Branch branch;

    private List<Team> teams;

    private int teamPlayerCount;

}
