package dev.mmkpc.tournamentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAddRequestDto {
    private Long teamId;
    private Long userId;
    private int jerseyNumber;

    // Constructors, getters, and setters
}