package com.willy.tictactoe.dto.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateGameRequest {

    @JsonProperty("game_size")
    @NotNull(message = "Size cannot be null") private Integer gameSize;
}
