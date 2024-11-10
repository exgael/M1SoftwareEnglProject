package org.sudokusolver.Gameplay.Reader;

import java.io.*;

public class FileReader {

    public String readFile(String filePath) throws RuntimeException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) {
                    content.append(value.trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }

        return content.toString();
    }
}



