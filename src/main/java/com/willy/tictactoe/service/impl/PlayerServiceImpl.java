package com.willy.tictactoe.service.impl;

import com.willy.tictactoe.model.Player;
import com.willy.tictactoe.repository.PlayerRepository;
import com.willy.tictactoe.service.PlayerService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player findById(Long playerId) {
        Optional<Player> playerData = playerRepository.findById(playerId);
        return playerData.orElse(null);
    }

    @Override
    public Player createNewPlayer(String username) {
        return playerRepository.save(new Player(username));
    }

    @Override
    public boolean isUsernameExists(String username) {
        return playerRepository.existsByUsername(username);
    }
}
