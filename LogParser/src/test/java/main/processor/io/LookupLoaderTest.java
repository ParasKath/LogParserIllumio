package main.processor.io;

import main.processor.data.PortProtocolKey;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;


class LookupLoaderTest {

    @Test
    void testLoadLookupTable() throws Exception {
        String testFile = "src/test/java/resources/test_lookup_table.txt";
        Map<PortProtocolKey, String> lookupTable = LookupLoader.loadLookupTable(testFile);

        assertEquals("web", lookupTable.get(new PortProtocolKey("443", "tcp")));
        assertEquals("ssh", lookupTable.get(new PortProtocolKey("22", "tcp")));
    }
}
