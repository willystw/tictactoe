package com.willy.tictactoe.dto.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("player_one_id")
    private Long playerOneId;

    @JsonProperty("player_two_id")
    private Long playerTwoId;

    @JsonProperty("is_started")
    private Boolean isStarted;

    @JsonProperty("is_finished")
    private Boolean isFinished;

    @JsonProperty("winner_id")
    private Long winnerId;

    @JsonProperty("created_timestamp")
    private Instant createdTimestamp;

    @JsonProperty("last_updated_timestamp")
    private Instant lastUpdatedTimestamp;
}
