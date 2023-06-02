package dev.mmkpc.tournamentapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @ToString.Exclude
//    @JsonManagedReference
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Team> teams;

    @Column(name = "TEAM_PLAYER_COUNT")
    private int teamPlayerCount;

    public Tournament(int year, Branch branch, List<Team> teams, int teamPlayerCount) {
        this.year = year;
        this.branch = branch;
        this.teams = teams;
        this.teamPlayerCount = teamPlayerCount;
    }
}