package com.Jobseeker.Jobseeker.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {
    public static String openFileWithData(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }
}
