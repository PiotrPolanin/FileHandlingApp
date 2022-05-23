package com.company.filehandlingapp.model.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StringFileOperation implements FileOperation<String> {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Override
    public List<String> read(String path) {
        if (path != null && !path.isEmpty()) {
            Path filePath = Paths.get(path);
            if (Files.exists(filePath)) {
                try {
                    return Files.readAllLines(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ArrayList<String>();
    }

    @Override
    public boolean write(String path, List<String> content) {
        if (path != null && !path.isEmpty() && content != null && content.size() > 0) {
            Path filePath = Paths.get(path);
            String stringSequence = String.join(LINE_SEPARATOR, content);
            try {
                Files.writeString(filePath, stringSequence);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
