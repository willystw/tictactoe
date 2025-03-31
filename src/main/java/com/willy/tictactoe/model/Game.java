package com.willy.tictactoe.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_one_id")
    @ToString.Exclude
    private Player playerOne;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_two_id")
    @ToString.Exclude
    private Player playerTwo;

    @Setter
    @Column(name = "is_started")
    private Boolean isStarted;

    @Setter
    @Column(name = "is_finished")
    private Boolean isFinished;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    @ToString.Exclude
    private Player winner;

    @CreationTimestamp
    @Column(name = "created_timestamp")
    private Instant createdTimestamp;

    @UpdateTimestamp
    @Column(name = "last_updated_timestamp")
    private Instant lastUpdatedTimestamp;

    @Setter
    @ToString.Exclude
    @OneToOne(mappedBy = "game", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Board board;

    // bidirectional relationship management method between game and board
    public Game setBoardData(Board board) {
        if (board == null) {
            if (this.board != null) {
                this.board.setGame(null);
            }
        } else {
            board.setGame(this);
        }
        this.board = board;
        return this;
    }
}
