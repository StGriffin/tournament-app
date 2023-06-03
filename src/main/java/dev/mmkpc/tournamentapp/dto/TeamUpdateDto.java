package dev.mmkpc.tournamentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamUpdateDto {

    private Long teamId;
    private String teamName;
    private Long teamLeadUserId;
    private List<Long> playerIds;
}
