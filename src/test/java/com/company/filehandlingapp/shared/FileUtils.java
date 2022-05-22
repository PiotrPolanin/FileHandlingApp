package com.company.filehandlingapp.shared;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static void removeFilesFromDirectory(String directoryPath) {
        Path dp = Path.of(directoryPath);
        try {
            List<Path> paths = Files.list(dp).collect(Collectors.toList());
            for (Path p : paths) {
                Files.deleteIfExists(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
