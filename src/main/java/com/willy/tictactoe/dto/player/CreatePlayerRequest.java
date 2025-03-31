package com.willy.tictactoe.dto.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreatePlayerRequest {
    @JsonProperty("username")
    @NotNull(message = "Username cannot be null") private String username;
}
