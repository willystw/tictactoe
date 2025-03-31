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
public class BoardResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("board")
    private char[][] board;

    @JsonProperty("turn")
    private int turn;
}
