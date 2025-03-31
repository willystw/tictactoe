package com.willy.tictactoe.service;

import com.willy.tictactoe.model.Game;

public interface GameService {
    Game saveGameData(Game game);

    Game getById(Long gameId);
}
