package com.willy.tictactoe.dto.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MakeMovementResponse {
    @JsonProperty("response")
    private BoardResponse boardResponse;

    @JsonProperty("is_valid")
    private boolean isValid;

    @JsonProperty("is_game_finished")
    private boolean isGameFinished;
}
