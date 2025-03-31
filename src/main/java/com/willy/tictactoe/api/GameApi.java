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
            log.warn("[action=GET_GAME] [gameId={}] Unable to view a game  | Cause: Invalid game ID", gameId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toGameResponse(game));
    }

    @PostMapping("/create")
    public ResponseEntity<GameResponse> createNewGame(
            @Valid @RequestBody CreateGameRequest createGameRequest, @RequestHeader(value = "x-user-id") Long userId) {
        Player firstPlayer = playerService.findById(userId);

        if (firstPlayer == null) {
            log.warn("[action=CREATE_GAME] [playerId={}] Unable to create a game  | Cause: Invalid player ID", userId);
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

        log.info("[action=CREATE_GAME] [playerId={}] [gameId={}] New game created ", userId, gameData.getId());
        return ResponseEntity.ok(toGameResponse(gameData));
    }

    @PostMapping("/join")
    public ResponseEntity<GameResponse> joinGame(
            @Valid @RequestBody JoinGameRequest joinGameRequest, @RequestHeader(value = "x-user-id") Long userId) {
        Player playerData = playerService.findById(userId);

        if (playerData == null) {
            log.warn(
                    "[action=JOIN_GAME] [playerId={}] [gameId={}] Unable to join a game | Cause: Invalid player ID",
                    userId,
                    joinGameRequest.getGameId());
            return ResponseEntity.notFound().build();
        }

        Game gameData = gameService.getById(joinGameRequest.getGameId());
        if (gameData == null) {
            log.warn(
                    "[action=JOIN_GAME] [gameId={}] [playerId={}] Unable to join a game | Cause: Invalid game ID",
                    joinGameRequest.getGameId(),
                    userId);
            return ResponseEntity.notFound().build();
        }
        if (gameData.getPlayerTwo() != null || gameData.getPlayerOne().getId().equals(userId)) {
            log.warn(
                    "[action=JOIN_GAME] [gameId={}] [playerId={}] Unable to join a game | Cause: Invalid second player data",
                    joinGameRequest.getGameId(),
                    userId);
            return ResponseEntity.badRequest().build();
        }
        if (gameData.getIsFinished() == Boolean.TRUE) {
            log.warn(
                    "[action=JOIN_GAME] [gameId={}] [playerId={}] Unable to join a game | Cause: game already finished",
                    joinGameRequest.getGameId(),
                    userId);
            return ResponseEntity.badRequest().build();
        }

        gameData.setPlayerTwo(playerData);
        gameData.setIsStarted(true);

        gameData = gameService.saveGameData(gameData);
        log.info("[action=JOIN_GAME] [playerId={}] [gameId={}] Player joined a game", userId, gameData.getId());
        return ResponseEntity.ok(toGameResponse(gameData));
    }

    @PostMapping("/quit")
    public ResponseEntity<GameResponse> quitGame(
            @Valid @RequestBody QuitGameRequest quitGameRequest, @RequestHeader(value = "x-user-id") Long userId) {
        Player surrenderPlayerData = playerService.findById(userId);

        if (surrenderPlayerData == null) {
            log.warn(
                    "[action=QUIT_GAME] [playerId={}] [gameId={}] Player ID not found  | Cause: Invalid player ID",
                    userId,
                    quitGameRequest.getGameId());
            return ResponseEntity.notFound().build();
        }

        Game gameData = gameService.getById(quitGameRequest.getGameId());
        if (gameData == null) {
            log.warn(
                    "[action=QUIT_GAME] [gameId={}] [playerId={}] Game ID not found  | Cause: Invalid game ID",
                    quitGameRequest.getGameId(),
                    userId);
            return ResponseEntity.notFound().build();
        }

        if (Boolean.TRUE.equals(gameData.getIsFinished()) || Boolean.FALSE.equals(gameData.getIsStarted())) {
            log.warn(
                    "[action=QUIT_GAME] [gameId={}] [playerId={}] Unable to quit a game  | Cause: Invalid game state",
                    quitGameRequest.getGameId(),
                    userId);
            return ResponseEntity.badRequest().build();
        }

        if (!gameData.getPlayerOne().getId().equals(userId)
                && !gameData.getPlayerTwo().getId().equals(userId)) {
            log.warn(
                    "[action=QUIT_GAME] [gameId={}] [playerId={}] Unable to quit game | Cause: Invalid player ID",
                    quitGameRequest.getGameId(),
                    userId);
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
        log.info(
                "[action=QUIT_GAME] [playerId={}] [gameId={}] Player exited a game ",
                userId,
                quitGameRequest.getGameId());
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
