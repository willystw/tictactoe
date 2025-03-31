package com.willy.tictactoe.repository;

import com.willy.tictactoe.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    boolean existsByUsername(String username);
}
