package dev.mmkpc.tournamentapp.dto;

import dev.mmkpc.tournamentapp.model.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentCreationDto {

    int year;
    Branch branch ;
    int teamPlayerCount;

}
