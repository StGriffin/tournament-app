package dev.mmkpc.tournamentapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TEAM_LEADERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamLeader{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TEAM_LEADER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;


    // Getter ve Setter metotları
}