package main.processor.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadConfigProperties {

    public static Properties loadProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        }
        return properties;
    }
}
