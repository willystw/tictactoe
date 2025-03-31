package com.willy.tictactoe.dto.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlayerRecordResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("player_id")
    private Long firstPlayerId;

    @JsonProperty("opponent_id")
    private Long secondPlayerId;

    @JsonProperty("is_win")
    private Boolean isFirstPlayerWon;

    @JsonProperty("timestamp")
    private Instant completionTimestamp;
}
