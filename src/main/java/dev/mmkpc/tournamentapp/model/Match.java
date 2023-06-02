package dev.mmkpc.tournamentapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "matches")
@Data
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATCH_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOME_TEAM", referencedColumnName = "TEAM_ID")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AWAY_TEAM_ID", referencedColumnName = "TEAM_ID")
    private Team awayTeam;

    @Column(name = "SCORE")
    private String score;

}
