package com.willy.tictactoe.service.impl;

import com.willy.tictactoe.model.Game;
import com.willy.tictactoe.repository.GameRepository;
import com.willy.tictactoe.service.GameService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game saveGameData(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Game getById(Long gameId) {
        Optional<Game> gameData = gameRepository.findById(gameId);
        return gameData.orElse(null);
    }
}
