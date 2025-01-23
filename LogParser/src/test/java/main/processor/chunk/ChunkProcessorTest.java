package main.processor.chunk;

import main.processor.data.PortProtocolKey;
import main.processor.data.ProcessingResult;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class ChunkProcessorTest {

    @Test
    void testProcessChunk() throws Exception {
        List<String> chunk = Arrays.asList(
                "2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK",
                "2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK"
        );

        Map<PortProtocolKey, String> lookupTable = Map.of(
                new PortProtocolKey("443", "tcp"), "web"
        );

        ProcessingResult result = new ProcessingResult();

        ChunkProcessor.processChunk(chunk, lookupTable, result, Executors.newSingleThreadExecutor()).join();

        assertEquals(2, result.getTagCounts().get("web"));
        assertEquals(2, result.getPortProtocolCounts().get("443:tcp"));
    }
}
