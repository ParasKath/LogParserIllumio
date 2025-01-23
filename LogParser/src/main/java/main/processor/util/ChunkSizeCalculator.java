package main.processor.util;

import java.io.File;
import java.util.Properties;

public class ChunkSizeCalculator {

    public static int calculateChunkSize(String filePath, Properties properties, int THREAD_COUNT) {
        long fileSizeBytes = new File(filePath).length();
        int averageLineSize = 200;
        try {
            averageLineSize = Integer.parseInt(properties.getProperty("averageLineSize", "200"));
        } catch (NumberFormatException e) {
            System.err.println("Invalid averageLineSize in properties. Using default: 200");
        }

        // Estimate number of lines in the file
        int estimatedLines = (int) Math.ceil((double) fileSizeBytes / averageLineSize);

        // Calculate chunk size
        return Math.max(1, estimatedLines / THREAD_COUNT);
    }
}
