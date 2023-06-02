package dev.mmkpc.tournamentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDeleteRequest {

    private Long teamId;
    private Long playerId;
}
