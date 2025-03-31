package com.willy.tictactoe.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class BoardConverterTest {

    private final BoardConverter converter = new BoardConverter();

    @Test
    void convertToDatabaseColumn_NullGrid_returnNull() {
        String s = converter.convertToDatabaseColumn(null);
        assertThat(s).isNull();
    }

    @Test
    void convertToDatabaseColumn_EmptyGrid_returnEmpty() {
        String s = converter.convertToDatabaseColumn(new char[0][0]);
        assertThat(s).isEmpty();
    }

    @Test
    void convertToDatabaseColumn_OneRowGrid_returnString() {
        char[][] board1 = {{'O', 'O', 'O'}};
        char[][] board2 = {{'-', 'O', 'O'}};
        char[][] board3 = {{'X', 'O', '-'}};

        assertThat(converter.convertToDatabaseColumn(board1)).isEqualTo("OOO");
        assertThat(converter.convertToDatabaseColumn(board2)).isEqualTo("-OO");
        assertThat(converter.convertToDatabaseColumn(board3)).isEqualTo("XO-");
    }

    @Test
    void convertToDatabaseColumn_MultipleRowGrid_returnStringWithLimiter() {
        char[][] board = {
            {'-', 'O', 'X'},
            {'X', 'O', '-'},
            {'-', 'O', '-'}
        };

        assertThat(converter.convertToDatabaseColumn(board)).isEqualTo("-OX\nXO-\n-O-");
    }

    @Test
    void convertToEntityAttribute_EmptyString_returnEmptyArray() {
        char[][] board = converter.convertToEntityAttribute("");
        assertThat(board).isEmpty();
    }

    @Test
    void convertToEntityAttribute_NullString_returnEmptyArray() {
        assertThat(converter.convertToEntityAttribute(null)).isEmpty();
    }

    @Test
    void convertToEntityAttribute_SingleRowString_returnValidBoard() {
        String input = "XXO";
        char[][] board = {{'X', 'X', 'O'}};

        assertThat(converter.convertToEntityAttribute(input)).isDeepEqualTo(board);
    }

    @Test
    void convertToEntityAttribute_MultipleRowString_returnValidBoard() {
        String input1 = "\nOXO\n-XX\n";
        char[][] board1 = {
            {}, {'O', 'X', 'O'}, {'-', 'X', 'X'},
        };

        String input2 = "XXO\nO-X\nOXO\n";
        char[][] board2 = {
            {'X', 'X', 'O'},
            {'O', '-', 'X'},
            {'O', 'X', 'O'},
        };

        assertThat(converter.convertToEntityAttribute(input1)).isDeepEqualTo(board1);
        assertThat(converter.convertToEntityAttribute(input2)).isDeepEqualTo(board2);
    }

    @Test
    void testConvert_ProducesCorrectBoard() {
        char[][] board = {
            {'X', 'X', 'O'},
            {'O', '-', 'X'},
            {'O', 'X', 'O'},
        };

        String convert = converter.convertToDatabaseColumn(board);
        char[][] convertedToBoard = converter.convertToEntityAttribute(convert);

        assertThat(convertedToBoard).isDeepEqualTo(board);
    }
}
