package com.willy.tictactoe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Entity
@Table(
        name = "player_records",
        indexes = {
            @Index(
                    name = "idx_first_player_id_completion_timestamp",
                    columnList = "first_player_id, completion_timestamp")
        })
public class PlayerRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(name = "first_player_id")
    private final Long firstPlayerId;

    @Column(name = "second_player_id")
    private final Long secondPlayerId;

    @Column(name = "is_first_player_won")
    private final Boolean isFirstPlayerWon;

    @CreationTimestamp
    @Column(name = "completion_timestamp")
    private Instant completionTimestamp;
}
