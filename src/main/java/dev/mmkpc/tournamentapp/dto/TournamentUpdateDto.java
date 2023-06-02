package dev.mmkpc.tournamentapp.dto;

import dev.mmkpc.tournamentapp.model.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentUpdateDto {
    private Long id;
    private int year;
    private Branch branch;
    private int teamPlayerCount;
    private List<Long> teams;

}
