package dev.mmkpc.tournamentapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "standings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Standings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STANDINGS_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOURNAMENT_ID")
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column(name = "MATCHES_PLAYED")
    private Integer matchesPlayed;

    @Column(name = "GOALS_SCORED")
    private Integer goalsScored;

    @Column(name = "GOALS_CONCEDED")
    private Integer goalsConceded;

    @Column(name = "GOAL_DIFFERENCE")
    private Integer goalDifference;

    @Column(name = "POINTS")
    private Integer points;
}