package com.willy.tictactoe.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class BoardConverter implements AttributeConverter<char[][], String> {
    private static final String ROW_DELIMITER = "\n";

    @Override
    public String convertToDatabaseColumn(char[][] chars) {
        if (chars == null) {
            return null;
        }

        return Arrays.stream(chars).map(String::new).collect(Collectors.joining(ROW_DELIMITER));
    }

    @Override
    public char[][] convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) return new char[0][0];
        return Arrays.stream(s.split(ROW_DELIMITER)).map(String::toCharArray).toArray(char[][]::new);
    }
}
