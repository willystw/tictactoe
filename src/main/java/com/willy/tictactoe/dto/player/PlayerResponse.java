package com.willy.tictactoe.dto.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlayerResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;
}
