package dev.mmkpc.tournamentapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "USER_INFO")
@Data
@NoArgsConstructor
@AllArgsConstructor
    public class User{
        @Id
        @Column(name="USER_ID")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "USERNAME",unique = true)
        private String userName;

        @Column(name = "PASSWORD")
        private String password;

        @Column(name = "FULL_NAME")
        private String fullName;

        @Enumerated(EnumType.STRING)
        private Role role;

        @Column(name = "AGE")
        private int age;


    public User(String userName, String password, String fullName, int age) {
        this.userName = userName;
        this.password=password;
        this.fullName=fullName;
        this.age=age;
        this.role=Role.NORMAL;
    }
}