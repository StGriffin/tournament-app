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
    private int matchesPlayed;

    @Column(name = "GOALS_SCORED")
    private int goalsScored;

    @Column(name = "GOALS_CONCEDED")
    private int goalsConceded;

    @Column(name = "GOAL_DIFFERENCE")
    private int goalDifference;

    @Column(name = "POINTS")
    private int points;
}