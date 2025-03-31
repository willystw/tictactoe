package com.willy.tictactoe.service;

import com.willy.tictactoe.model.Board;

public interface BoardService {
    void saveBoardData(Board board);

    Board getBoard(Long id);
}
