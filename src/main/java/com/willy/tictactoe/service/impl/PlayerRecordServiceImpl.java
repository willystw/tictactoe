package com.willy.tictactoe.service.impl;

import com.willy.tictactoe.model.Player;
import com.willy.tictactoe.model.PlayerRecord;
import com.willy.tictactoe.repository.PlayerRecordRepository;
import com.willy.tictactoe.service.PlayerRecordService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlayerRecordServiceImpl implements PlayerRecordService {
    private final PlayerRecordRepository playerRecordRepository;

    public PlayerRecordServiceImpl(PlayerRecordRepository playerRecordRepository) {
        this.playerRecordRepository = playerRecordRepository;
    }

    @Override
    public List<PlayerRecord> getPlayerHistory(Long playerId) {
        return playerRecordRepository.findAllByFirstPlayerIdOrderByCompletionTimestampDesc(playerId);
    }

    @Override
    public void insertMatchHistory(Player firstPlayer, Player secondPlayer, Long winnerId) {
        // Determine the loser based on the winner
        Long loserId = firstPlayer.getId().equals(winnerId) ? secondPlayer.getId() : firstPlayer.getId();

        // Save records for both players
        playerRecordRepository.save(new PlayerRecord(loserId, winnerId, false)); // Loser record
        playerRecordRepository.save(new PlayerRecord(winnerId, loserId, true)); // Winner record
    }
}
