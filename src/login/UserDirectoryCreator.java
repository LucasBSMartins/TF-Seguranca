package src.login;

import java.io.IOException;
import java.nio.file.*;

public class UserDirectoryCreator {

    public void createUserDirectory(String username, String basePath) {

        String sanitizedUsername = username.replace(" ", "_");

        Path directoryPath = Paths.get(basePath);
        Path filePath = directoryPath.resolve(sanitizedUsername+".p12");

        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.out.println("Error creating directory or file: " + e.getMessage());
        }
    }
}
