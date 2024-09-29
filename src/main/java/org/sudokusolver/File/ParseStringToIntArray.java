package org.sudokusolver.File;

public class ParseStringToIntArray {

    public int[] parseString(String stringFile) {

        // Remove all spaces and commas
        stringFile = stringFile.replaceAll("[\\s,]+", "");

        if (stringFile.length() != 81) {
            throw new IllegalArgumentException("La longueur doit etre de 81 char");
        }

        int[] board = new int[81];
        for (int i = 0; i < 81; i++) {
            char c = stringFile.charAt(i);
            if (Character.isDigit(c)) {
                board[i] = Character.getNumericValue(c);
            } else {
                throw new IllegalArgumentException("Le fichier doit contenir uniquement des int");
            }
        }

        return board;
    }
}

