package com.willy.tictactoe.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BoardTest {
    private static final char X = 'X';
    private static final char O = 'O';

    @Test
    public void testHorizontalWin3x3() {
        Board board = new Board(3);
        // X | X | X
        //   |   |
        //   |   |
        board.move(0, 0, X);
        board.move(0, 1, X);
        board.move(0, 2, X);
        assertTrue(board.isWinState(0, 2));
    }

    @Test
    public void testVerticalWin3x3() {
        Board board = new Board(3);
        // X |   |
        // X |   |
        // X |   |
        board.move(0, 0, X);
        board.move(1, 0, X);
        board.move(2, 0, X);
        assertTrue(board.isWinState(2, 0));
    }

    @Test
    public void testMainDiagonalWin3x3() {
        Board board = new Board(3);
        // X |   |
        //   | X |
        //   |   | X
        board.move(0, 0, X);
        board.move(1, 1, X);
        board.move(2, 2, X);
        assertTrue(board.isWinState(2, 2));
    }

    @Test
    public void testAntiDiagonalWin3x3() {
        Board board = new Board(3);
        //   |   | X
        //   | X |
        // X |   |
        board.move(0, 2, X);
        board.move(1, 1, X);
        board.move(2, 0, X);
        assertTrue(board.isWinState(2, 0));
    }

    @Test
    public void testNoWinState3x3() {
        Board board = new Board(3);
        // X | O |
        //   | X |
        //   |   | O
        board.move(0, 0, X);
        board.move(0, 1, O);
        board.move(1, 1, X);
        board.move(2, 2, O);
        assertFalse(board.isWinState(2, 2));
    }

    @Test
    public void testHorizontalWin5x5() {
        Board board = new Board(5);
        for (int col = 0; col < 5; col++) {
            board.move(2, col, X);
        }
        assertTrue(board.isWinState(2, 4));
    }

    @Test
    public void testVerticalWin5x5() {
        Board board = new Board(5);
        for (int row = 0; row < 5; row++) {
            board.move(row, 3, O);
        }
        assertTrue(board.isWinState(4, 3));
    }

    @Test
    public void testDiagonalWin5x5() {
        Board board = new Board(5);
        for (int i = 0; i < 5; i++) {
            board.move(i, i, X);
        }
        assertTrue(board.isWinState(4, 4));
    }

    @Test
    public void testEdgeCaseCornerWin() {
        Board board = new Board(3);
        // X | X |
        // X |   |
        // X |   |
        board.move(0, 0, X);
        board.move(0, 1, X);
        board.move(1, 0, X);
        board.move(2, 0, X);
        assertTrue(board.isWinState(2, 0));
    }

    @Test
    public void testPartialLineNoWin() {
        Board board = new Board(4);
        // X | X | X |
        //   |   |   |
        //   |   |   |
        //   |   |   |
        board.move(0, 0, X);
        board.move(0, 1, X);
        board.move(0, 2, X);
        assertFalse(board.isWinState(0, 2));
    }

    @Test
    public void testMixedSymbolsWin() {
        Board board = new Board(3);
        // X | O | X
        // O | X | O
        // X | O | X
        board.move(0, 0, X);
        board.move(0, 1, O);
        board.move(0, 2, X);
        board.move(1, 0, O);
        board.move(1, 1, X);
        board.move(1, 2, O);
        board.move(2, 0, X);
        board.move(2, 1, O);
        board.move(2, 2, X);
        assertTrue(board.isWinState(2, 2));
    }

    @Test
    public void testMixedSymbolsNoWin() {
        Board board = new Board(3);
        // X | O | X
        // X | O | O
        // O | X | X
        board.move(0, 0, X);
        board.move(0, 1, O);
        board.move(0, 2, X);
        board.move(1, 0, X);
        board.move(1, 1, O);
        board.move(1, 2, O);
        board.move(2, 0, O);
        board.move(2, 1, X);
        board.move(2, 2, X);
        assertFalse(board.isWinState(2, 2));
        assertFalse(board.isWinState(1, 1));
        assertFalse(board.isWinState(0, 0));
    }

    @Test
    public void testEmptyCellReturnsFalse() {
        Board board = new Board(3);
        assertFalse(board.isWinState(1, 1));
    }
}
