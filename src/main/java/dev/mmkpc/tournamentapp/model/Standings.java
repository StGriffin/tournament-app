package dev.mmkpc.tournamentapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOURNAMENT_ID")
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column(name = "MATCHES_PLAYED")
    private int matchesPlayed;

    @Column(name = "MATCHES_WIN")
    private int wins;

    @Column(name = "MATCHES_LOSE")
    private int losses;

    @Column(name = "MATCHES_DRAW")
    private int draws;

    @Column(name = "GOALS_SCORED")
    private int goalsScored;

    @Column(name = "GOALS_CONCEDED")
    private int goalsConceded;

    @Column(name = "GOAL_DIFFERENCE")
    private int goalDifference;

    @Column(name = "POINTS")
    private int points;
}