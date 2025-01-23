package main.processor.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessingResultTest {

    @Test
    void testIncrementTagCount() {
        ProcessingResult result = new ProcessingResult();
        result.incrementTagCount("web");
        result.incrementTagCount("web");

        assertEquals(2, result.getTagCounts().get("web"));
    }

    @Test
    void testIncrementPortProtocolCount() {
        ProcessingResult result = new ProcessingResult();
        result.incrementPortProtocolCount("443", "tcp");

        assertEquals(1, result.getPortProtocolCounts().get("443:tcp"));
    }
}

