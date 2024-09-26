package org.sudokusolver;

import java.io.*;

public class ReadFile {

    public static int[] readFile(String fileName) throws IOException {

        if (!fileName.endsWith(".txt")) {
            System.err.println("Erreur : le fichier doit etre un .txt");
        }

        int[] sudokuGrid = new int[81];
        File file = new File(fileName);

        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            int index = 0;

            System.out.println("Lecture du fichier :");
            while ((line = bufferedReader.readLine()) != null) {
                String[] numbers = line.split(",");

                for (int n = 0; n < 9; n++) {
                    sudokuGrid[index] = Integer.parseInt(numbers[n].trim());
                    index++;
                }
            }

        } catch (IOException e) {
            throw new IOException("Impossible de lire le fichier : " + fileName, e);
        }
        return(sudokuGrid);
    }
}


