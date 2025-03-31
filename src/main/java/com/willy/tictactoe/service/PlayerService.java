package com.willy.tictactoe.service;

import com.willy.tictactoe.model.Player;

public interface PlayerService {

    Player findById(Long playerId);

    Player createNewPlayer(String username);

    boolean isUsernameExists(String username);
}
