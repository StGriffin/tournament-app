package dev.mmkpc.tournamentapp.security.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import dev.mmkpc.tournamentapp.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;

  @JsonIgnore
  private String password;

  private String fullName;

  private int age;

  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(Long id, String username, String password,String fullName,int age, Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.fullName=fullName;
    this.age=age;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(User user) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

    return new UserDetailsImpl(user.getId(), 
                               user.getUserName(),
                               user.getPassword(),
                               user.getFullName(),
                               user.getAge(),
                               authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public String getFullName(){
    return fullName;
  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}