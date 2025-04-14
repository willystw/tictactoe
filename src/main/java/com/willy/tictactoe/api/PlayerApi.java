package com.willy.tictactoe.api;

import com.willy.tictactoe.dto.player.CreatePlayerRequest;
import com.willy.tictactoe.dto.player.PlayerRecordResponse;
import com.willy.tictactoe.dto.player.PlayerResponse;
import com.willy.tictactoe.model.Player;
import com.willy.tictactoe.model.PlayerRecord;
import com.willy.tictactoe.service.PlayerRecordService;
import com.willy.tictactoe.service.PlayerService;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
@Slf4j
public class PlayerApi {

    private final PlayerService playerService;
    private final PlayerRecordService playerRecordService;

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable Long playerId) {
        Player player = playerService.findById(playerId);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toPlayerResponse(player));
    }

    @PostMapping("/create")
    public ResponseEntity<PlayerResponse> createNewPlayer(@Valid @RequestBody CreatePlayerRequest createPlayerRequest) {
        String username = createPlayerRequest.getUsername().trim().toLowerCase();

        if (playerService.isUsernameExists(username)) {
            log.atWarn()
                    .addKeyValue("action", "CREATE_PLAYER")
                    .addKeyValue("username", username)
                    .setMessage("Unable to create new player | Cause: username already exists")
                    .log();
            return ResponseEntity.badRequest().build();
        }

        Player p = playerService.createNewPlayer(username);
        log.atInfo()
                .addKeyValue("action", "CREATE_PLAYER")
                .addKeyValue("player_id", p.getId())
                .setMessage("Player created")
                .log();
        return ResponseEntity.ok(toPlayerResponse(p));
    }

    @GetMapping("/get/{playerId}")
    public ResponseEntity<Player> getPlayerData(@PathVariable Long playerId) {
        if (playerService.findById(playerId) == null) {
            return ResponseEntity.notFound().build();
        }

        Player player = playerService.findById(playerId);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(player);
    }

    @GetMapping("/history/{playerId}")
    public ResponseEntity<Collection<PlayerRecordResponse>> getPlayerHistory(@PathVariable Long playerId) {
        if (playerService.findById(playerId) == null) {
            return ResponseEntity.notFound().build();
        }

        Collection<PlayerRecordResponse> records = playerRecordService.getPlayerHistory(playerId).stream()
                .map(this::toPlayerRecordResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(records);
    }

    private PlayerRecordResponse toPlayerRecordResponse(PlayerRecord record) {
        return new PlayerRecordResponse(
                record.getId(),
                record.getFirstPlayerId(),
                record.getSecondPlayerId(),
                record.getIsFirstPlayerWon(),
                record.getCompletionTimestamp());
    }

    private PlayerResponse toPlayerResponse(Player player) {
        return new PlayerResponse(player.getId(), player.getUsername());
    }
}
