package com.willy.tictactoe.api;

import com.willy.tictactoe.dto.game.CreateGameRequest;
import com.willy.tictactoe.dto.game.GameResponse;
import com.willy.tictactoe.dto.game.JoinGameRequest;
import com.willy.tictactoe.dto.game.QuitGameRequest;
import com.willy.tictactoe.model.Board;
import com.willy.tictactoe.model.Game;
import com.willy.tictactoe.model.Player;
import com.willy.tictactoe.service.BoardService;
import com.willy.tictactoe.service.GameService;
import com.willy.tictactoe.service.PlayerRecordService;
import com.willy.tictactoe.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/games")
@Slf4j
@RequiredArgsConstructor
public class GameApi {

    private final GameService gameService;
    private final BoardService boardService;
    private final PlayerService playerService;
    private final PlayerRecordService playerRecordService;

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGameData(@PathVariable Long gameId) {
        Game game = gameService.getById(gameId);
        if (game == null) {
            log.atWarn()
                    .addKeyValue("action", "GET_GAME")
                    .addKeyValue("game_id", gameId)
                    .setMessage("Unable to view a game  | Cause: Invalid game ID")
                    .log();
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toGameResponse(game));
    }

    @PostMapping("/create")
    public ResponseEntity<GameResponse> createNewGame(
            @Valid @RequestBody CreateGameRequest createGameRequest, @RequestHeader(value = "x-user-id") Long userId) {
        Player firstPlayer = playerService.findById(userId);

        if (firstPlayer == null) {
            log.atWarn()
                    .addKeyValue("action", "CREATE_GAME")
                    .addKeyValue("player_id", userId)
                    .setMessage("Unable to create a game  | Cause: Invalid player ID")
                    .log();
            return ResponseEntity.notFound().build();
        }

        Board board = new Board(createGameRequest.getGameSize());

        Game gameData = new Game();
        gameData.setPlayerOne(firstPlayer);
        gameData.setIsStarted(false);
        gameData.setIsFinished(false);
        gameData.setBoardData(board);

        boardService.saveBoardData(board);
        gameData = gameService.saveGameData(gameData);

        log.atInfo()
                .addKeyValue("action", "CREATE_GAME")
                .addKeyValue("player_id", userId)
                .addKeyValue("game_id", gameData.getId())
                .setMessage("New game created")
                .log();
        return ResponseEntity.ok(toGameResponse(gameData));
    }

    @PostMapping("/join")
    public ResponseEntity<GameResponse> joinGame(
            @Valid @RequestBody JoinGameRequest joinGameRequest, @RequestHeader(value = "x-user-id") Long userId) {
        Player playerData = playerService.findById(userId);

        if (playerData == null) {
            log.atWarn()
                    .addKeyValue("action", "JOIN_GAME")
                    .addKeyValue("player_id", userId)
                    .addKeyValue("game_id", joinGameRequest.getGameId())
                    .setMessage("Unable to join a game | Cause: Invalid player ID")
                    .log();
            return ResponseEntity.notFound().build();
        }

        Game gameData = gameService.getById(joinGameRequest.getGameId());
        if (gameData == null) {
            log.atWarn()
                    .addKeyValue("action", "JOIN_GAME")
                    .addKeyValue("player_id", userId)
                    .addKeyValue("game_id", joinGameRequest.getGameId())
                    .setMessage("Unable to join a game | Cause: Invalid game ID")
                    .log();
            return ResponseEntity.notFound().build();
        }
        if (gameData.getPlayerTwo() != null || gameData.getPlayerOne().getId().equals(userId)) {
            log.atWarn()
                    .addKeyValue("action", "JOIN_GAME")
                    .addKeyValue("player_id", userId)
                    .addKeyValue("game_id", joinGameRequest.getGameId())
                    .setMessage("Unable to join a game | Cause: Invalid second player data")
                    .log();
            return ResponseEntity.badRequest().build();
        }
        if (gameData.getIsFinished() == Boolean.TRUE) {
            log.atWarn()
                    .addKeyValue("action", "JOIN_GAME")
                    .addKeyValue("player_id", userId)
                    .addKeyValue("game_id", joinGameRequest.getGameId())
                    .setMessage("Unable to join a game | Cause: game already finished")
                    .log();
            return ResponseEntity.badRequest().build();
        }

        gameData.setPlayerTwo(playerData);
        gameData.setIsStarted(true);

        gameData = gameService.saveGameData(gameData);
        log.atInfo()
                .addKeyValue("action", "JOIN_GAME")
                .addKeyValue("player_id", userId)
                .addKeyValue("game_id", joinGameRequest.getGameId())
                .setMessage("Player joined a game")
                .log();
        return ResponseEntity.ok(toGameResponse(gameData));
    }

    @PostMapping("/quit")
    public ResponseEntity<GameResponse> quitGame(
            @Valid @RequestBody QuitGameRequest quitGameRequest, @RequestHeader(value = "x-user-id") Long userId) {
        Player surrenderPlayerData = playerService.findById(userId);

        if (surrenderPlayerData == null) {
            log.atWarn()
                    .addKeyValue("action", "QUIT_GAME")
                    .addKeyValue("player_id", userId)
                    .addKeyValue("game_id", quitGameRequest.getGameId())
                    .setMessage("Player ID not found | Cause: Invalid player ID")
                    .log();
            return ResponseEntity.notFound().build();
        }

        Game gameData = gameService.getById(quitGameRequest.getGameId());
        if (gameData == null) {
            log.atWarn()
                    .addKeyValue("action", "QUIT_GAME")
                    .addKeyValue("game_id", quitGameRequest.getGameId())
                    .addKeyValue("player_id", userId)
                    .setMessage("Game ID not found | Cause: Invalid game ID")
                    .log();
            return ResponseEntity.notFound().build();
        }

        if (Boolean.TRUE.equals(gameData.getIsFinished()) || Boolean.FALSE.equals(gameData.getIsStarted())) {
            log.atWarn()
                    .addKeyValue("action", "QUIT_GAME")
                    .addKeyValue("game_id", quitGameRequest.getGameId())
                    .addKeyValue("player_id", userId)
                    .setMessage("Unable to quit a game | Cause: Invalid game state")
                    .log();
            return ResponseEntity.badRequest().build();
        }

        if (!gameData.getPlayerOne().getId().equals(userId)
                && !gameData.getPlayerTwo().getId().equals(userId)) {
            log.atWarn()
                    .addKeyValue("action", "QUIT_GAME")
                    .addKeyValue("game_id", quitGameRequest.getGameId())
                    .addKeyValue("player_id", userId)
                    .setMessage("Unable to quit game | Cause: Invalid player ID")
                    .log();
            return ResponseEntity.badRequest().build();
        }

        gameData.setIsFinished(true);

        gameData = gameService.saveGameData(gameData);
        if (gameData.getPlayerTwo() != null) {
            // save quit data as record
            if (gameData.getPlayerOne().equals(surrenderPlayerData)) {
                gameData.setWinner(gameData.getPlayerTwo());
            } else if (gameData.getPlayerTwo().equals(surrenderPlayerData)) {
                gameData.setWinner(gameData.getPlayerOne());
            }
            playerRecordService.insertMatchHistory(
                    gameData.getPlayerOne(),
                    gameData.getPlayerTwo(),
                    gameData.getWinner().getId());
        }
        log.atInfo()
                .addKeyValue("action", "QUIT_GAME")
                .addKeyValue("player_id", userId)
                .addKeyValue("game_id", quitGameRequest.getGameId())
                .setMessage("Player exited a game")
                .log();
        return ResponseEntity.ok(toGameResponse(gameData));
    }

    private GameResponse toGameResponse(Game game) {
        return new GameResponse(
                game.getId(),
                game.getPlayerOne().getId(),
                game.getPlayerTwo() != null ? game.getPlayerTwo().getId() : null,
                game.getIsStarted(),
                game.getIsFinished(),
                game.getWinner() != null ? game.getWinner().getId() : null,
                game.getCreatedTimestamp(),
                game.getLastUpdatedTimestamp());
    }
}
