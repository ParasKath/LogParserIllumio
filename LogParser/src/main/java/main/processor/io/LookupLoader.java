package main.processor.io;


import main.processor.data.PortProtocolKey;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LookupLoader {

    public static Map<PortProtocolKey, String> loadLookupTable(String lookupFile) throws IOException {
        Map<PortProtocolKey, String> lookupTable = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(lookupFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String dstport = parts[0].trim();
                String protocol = parts[1].trim();
                String tag = parts[2].trim();

                lookupTable.put(new PortProtocolKey(dstport, protocol), tag);
            }
        }

        return lookupTable;
    }
}

