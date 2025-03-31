package com.willy.tictactoe.model;

import com.willy.tictactoe.converter.BoardConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.Arrays;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "boards")
public class Board {
    public static final char DELIMITER = '-';

    @Id
    private Long id;

    @Convert(converter = BoardConverter.class)
    @Column(name = "board", columnDefinition = "TEXT")
    private char[][] board;

    @Column(name = "size")
    private int size;

    @Column(name = "turn")
    @Setter
    private int turn;

    @CreationTimestamp
    @Column(name = "created_timestamp")
    private Instant createdTimestamp;

    @UpdateTimestamp
    @Column(name = "last_updated_timestamp")
    private Instant lastUpdatedTimestamp;

    @Version
    @ToString.Exclude
    @Column(name = "version")
    private Long version;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Game game;

    public Board(int size) {
        this.size = size;
        this.board = new char[size][size];
        this.turn = 1;

        for (char[] boardValue : board) {
            Arrays.fill(boardValue, DELIMITER);
        }
    }

    public boolean move(int row, int column, char symbol) {
        if (row >= 0 && column >= 0 && row < size && column < size && board[row][column] == DELIMITER) {
            board[row][column] = symbol;
            return true;
        }
        return false;
    }

    public boolean isWinState(int row, int column) {
        char symbol = board[row][column];
        if (symbol == DELIMITER) {
            return false;
        }

        // Check all possible winning directions
        return checkDirection(row, column, 0, 1, symbol) // Horizontal
                || checkDirection(row, column, 1, 0, symbol) // Vertical
                || checkDirection(row, column, 1, 1, symbol) // Diagonal \
                || checkDirection(row, column, 1, -1, symbol); // Diagonal /
    }

    private boolean checkDirection(int row, int col, int rowDelta, int colDelta, char symbol) {
        int count = 1; // Start with current cell

        // Check in positive direction
        int r = row + rowDelta;
        int c = col + colDelta;
        while (isValidPosition(r, c) && board[r][c] == symbol) {
            count++;
            r += rowDelta;
            c += colDelta;
        }

        // Check in negative direction
        r = row - rowDelta;
        c = col - colDelta;
        while (isValidPosition(r, c) && board[r][c] == symbol) {
            count++;
            r -= rowDelta;
            c -= colDelta;
        }

        return count >= size;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
}
