package dev.mmkpc.tournamentapp.repository;

import dev.mmkpc.tournamentapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<Object> findByuserName(String username);

    boolean existsByuserName(String userName);

    List<User> findAllByIdNotIn(List<Long> userIds);
}
