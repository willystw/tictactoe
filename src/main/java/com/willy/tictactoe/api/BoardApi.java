package com.willy.tictactoe.api;

import com.willy.tictactoe.dto.board.BoardResponse;
import com.willy.tictactoe.dto.board.MakeMovementRequest;
import com.willy.tictactoe.dto.board.MakeMovementResponse;
import com.willy.tictactoe.model.Board;
import com.willy.tictactoe.model.Game;
import com.willy.tictactoe.model.Player;
import com.willy.tictactoe.repository.PlayerRecordRepository;
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
@RequestMapping("/api/v1/boards")
@Slf4j
@RequiredArgsConstructor
public class BoardApi {
    private static final char FIRST_PLAYER_SYMBOL = 'O';
    private static final char SECOND_PLAYER_SYMBOL = 'X';

    private final PlayerRecordRepository playerRecordRepository;

    private final BoardService boardService;
    private final GameService gameService;
    private final PlayerService playerService;
    private final PlayerRecordService playerRecordService;

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoardData(@PathVariable Long id) {
        Game game = gameService.getById(id);
        if (game == null) {
            log.warn("[action=GET_BOARD] [gameId={}] Invalid movement | Cause: Game data can not be found", id);
            return ResponseEntity.notFound().build();
        }
        Board board = game.getBoard();

        return ResponseEntity.ok().body(toResponse(board));
    }

    @PostMapping("/move/{boardID}")
    public ResponseEntity<MakeMovementResponse> makeMovement(
            @PathVariable Long boardID,
            @RequestHeader(value = "x-user-id") Long userId,
            @Valid @RequestBody MakeMovementRequest request) {
        Player player = playerService.findById(userId);
        Board board = boardService.getBoard(boardID);

        Game game = gameService.getById(boardID);
        if (game == null || board == null) {
            log.warn(
                    "[action=MAKE_MOVEMENT] [playerId={}] [gameId={}] Invalid movement | Cause: Game data can not be found",
                    userId,
                    boardID);
            return ResponseEntity.notFound().build();
        }

        if (game.getIsStarted() == Boolean.FALSE || game.getIsFinished() == Boolean.TRUE) {
            log.warn(
                    "[action=MAKE_MOVEMENT] [playerId={}] [gameId={}] Invalid movement | Cause: Invalid game state",
                    userId,
                    boardID);
            return ResponseEntity.badRequest().build();
        }

        game.setBoardData(board);

        int turn = board.getTurn();
        if (isPlayerTurn(player, turn, game)) {
            char symbol = turn == 1 ? FIRST_PLAYER_SYMBOL : SECOND_PLAYER_SYMBOL;
            boolean successMove = board.move(request.getRow(), request.getCol(), symbol);
            if (!successMove) {
                log.info(
                        "[action=MAKE_MOVEMENT] [playerId={}] [gameId={}] Player failed to make a movement | Cause: Invalid input or replacing existing symbol",
                        userId,
                        boardID);
                return ResponseEntity.ok(new MakeMovementResponse(toResponse(board), false, false));
            }

            updateGameState(board, game, request);

            gameService.saveGameData(game);
            boardService.saveBoardData(board);

            log.info("[action=MAKE_MOVEMENT] [playerId={}] [gameId={}] Player made a movement", userId, boardID);
            return ResponseEntity.ok(new MakeMovementResponse(
                    toResponse(board), true, game.getIsFinished() != null ? game.getIsFinished() : false));
        } else {
            log.warn(
                    "[action=MAKE_MOVEMENT] [playerId={}] [gameId={}] Invalid movement | Cause: Invalid player turn",
                    userId,
                    boardID);
            return ResponseEntity.badRequest().build();
        }
    }

    private BoardResponse toResponse(Board board) {
        return new BoardResponse(board.getId(), board.getBoard(), board.getTurn());
    }

    private boolean isPlayerTurn(Player player, int turn, Game game) {
        return (turn == 1 && game.getPlayerOne().equals(player))
                || (turn == 2 && game.getPlayerTwo().equals(player));
    }

    private void updateGameState(Board board, Game game, MakeMovementRequest request) {
        int currentTurn = board.getTurn();
        board.setTurn(currentTurn == 1 ? 2 : 1); // Switch turns

        if (board.isWinState(request.getRow(), request.getCol())) {
            game.setIsFinished(true);
            game.setWinner(currentTurn == 1 ? game.getPlayerOne() : game.getPlayerTwo());

            playerRecordService.insertMatchHistory(
                    game.getPlayerOne(), game.getPlayerTwo(), game.getWinner().getId());
        }
    }
}
