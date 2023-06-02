package dev.mmkpc.tournamentapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TEAM_INFO")
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOURNAMENT_ID")
    private Tournament tournament;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_LEADER_ID")
    private TeamLeader teamLeader;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamPlayer> players;

    @Column(name = "NAME")
    private String name;

}
