package com.willy.tictactoe.repository;

import com.willy.tictactoe.model.PlayerRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRecordRepository extends JpaRepository<PlayerRecord, Long> {

    List<PlayerRecord> findAllByFirstPlayerIdOrderByCompletionTimestampDesc(Long firstPlayerId);
}
