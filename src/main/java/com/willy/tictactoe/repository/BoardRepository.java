package com.willy.tictactoe.repository;

import com.willy.tictactoe.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {}
