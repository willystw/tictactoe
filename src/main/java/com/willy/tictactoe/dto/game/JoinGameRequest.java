package com.willy.tictactoe.dto.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class JoinGameRequest {
    @JsonProperty("game_id")
    @NotNull(message = "Game Id cannot be null") private Long gameId;
}
