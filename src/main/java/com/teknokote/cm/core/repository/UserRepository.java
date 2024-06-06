package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findAllByUsernameIgnoreCase(String name);

    @Modifying
    @Query("update User set lastConnectionDate= :connectionDate where username= :userName")
    void updateLastConnection(String userName, LocalDateTime connectionDate);
}
