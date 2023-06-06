package dev.mmkpc.tournamentapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "tournaments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOURNAMENT_ID")
    private Long id;

    @Column(name = "YEAR")
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(name = "BRANCH")
    private Branch branch;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TOURNAMENT_ID")
    private List<Team> teams;

    @Column(name = "TEAM_PLAYER_COUNT")
    private int teamPlayerCount;

    @Column(name = "IS_ACTIVE")
    private boolean isActive = false;

    public Tournament(int year, Branch branch, List<Team> teams, int teamPlayerCount) {
        this.year = year;
        this.branch = branch;
        this.teams = teams;
        this.teamPlayerCount = teamPlayerCount;
    }
}