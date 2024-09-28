package org.sudokusolver.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadFileFromResources {

    private static final Logger logger = Logger.getLogger(ReadFileFromResources.class.getName());

    public String readFile(String fileName) {
        StringBuilder content = new StringBuilder();

        // Récupérer le flux d'entrée
        try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                logger.log(Level.SEVERE, "Fichier non trouvé : " + fileName);
                return ""; // Renvoie une chaîne vide si le fichier n'est pas trouvé
            }

            // Créer le BufferedReader si le fichier est trouvé
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    for (String value : values) {
                        content.append(value.trim());
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erreur lors de la lecture du fichier : " + fileName, e);
        }

        return content.toString(); // Renvoie la chaîne de caractères sans virgules
    }
}



