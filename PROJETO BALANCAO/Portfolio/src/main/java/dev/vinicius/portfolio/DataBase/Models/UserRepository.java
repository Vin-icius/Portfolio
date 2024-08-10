package dev.vinicius.portfolio.DataBase.Models;
import dev.vinicius.portfolio.DataBase.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}