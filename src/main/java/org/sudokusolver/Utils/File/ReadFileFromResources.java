package org.sudokusolver.Utils.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadFileFromResources {

    public String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();

        InputStream inputStream = getClass().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("Fichier non trouvé: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) {
                    content.append(value.trim());
                }
            }
        }

        return content.toString();
    }
}



