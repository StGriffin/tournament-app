package dev.mmkpc.tournamentapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "TEAM_INFO")
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_LEADER_ID")
    private TeamLeader teamLeader;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TEAM_ID")
    private List<TeamPlayer> players;

    @Column(name = "NAME")
    private String name;

}
