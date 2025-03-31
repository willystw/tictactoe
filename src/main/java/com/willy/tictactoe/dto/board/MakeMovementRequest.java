package com.willy.tictactoe.dto.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MakeMovementRequest {
    @JsonProperty("row")
    @NotNull(message = "Row cannot be null") private Integer row;

    @JsonProperty("col")
    @NotNull(message = "Col cannot be null") private Integer col;
}
