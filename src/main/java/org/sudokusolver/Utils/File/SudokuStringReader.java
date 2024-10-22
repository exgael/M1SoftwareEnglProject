package org.sudokusolver.Utils.File;

import org.sudokusolver.Gameplay.SudokuReader;

public class SudokuStringReader implements SudokuReader {

    @Override
    public int[] readGridFrom(String str) throws RuntimeException {
        str = str.replaceAll("[\\s,]+", "");

        if (str.length() != 81) {
            throw new IllegalArgumentException("La longueur doit etre de 81 char");
        }

        int[] board = new int[81];
        for (int i = 0; i < 81; i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                board[i] = Character.getNumericValue(c);
            } else {
                throw new IllegalArgumentException("Le fichier doit contenir uniquement des int");
            }
        }
        return board;
    }
}

