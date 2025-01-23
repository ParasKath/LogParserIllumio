package main.processor.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class OutputWriter {

    public static void writeTagCounts(Map<String, Integer> tagCounts, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Tag,Count");
            writer.newLine();
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        }
    }

    public static void writePortProtocolCounts(Map<String, Integer> portProtocolCounts, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Port,Protocol,Count");
            writer.newLine();
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        }
    }
}

