package com.willy.tictactoe.repository;

import com.willy.tictactoe.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {}
