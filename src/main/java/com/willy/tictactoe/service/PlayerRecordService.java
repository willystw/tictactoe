package com.willy.tictactoe.service;

import com.willy.tictactoe.model.Player;
import com.willy.tictactoe.model.PlayerRecord;
import java.util.List;

public interface PlayerRecordService {
    List<PlayerRecord> getPlayerHistory(Long playerId);

    void insertMatchHistory(Player firstPlayer, Player secondPlayer, Long winnerId);
}
