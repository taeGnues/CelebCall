package org.portfolio.ourverse.src.persist;

import org.portfolio.ourverse.src.persist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    Optional<User> findByUsername(String username);
}
