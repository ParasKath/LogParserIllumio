package main.processor.chunk;

import main.processor.util.ProtocolMapper;
import main.processor.data.PortProtocolKey;
import main.processor.data.ProcessingResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ChunkProcessor {

    public static CompletableFuture<Void> processChunk(List<String> chunk,
                                                       Map<PortProtocolKey, String> lookupTable,
                                                       ProcessingResult sharedResult,
                                                       ExecutorService executor) {
        return CompletableFuture.runAsync(() -> {
            for (String line : chunk) {
                String[] fields = line.split(" ");
                if (fields.length < 8) continue;

                String dstport = fields[5];
                String protocol = ProtocolMapper.mapProtocolNumber(fields[7]);
                if (protocol == null) continue;

                PortProtocolKey key = new PortProtocolKey(dstport, protocol);
                String tag = lookupTable.getOrDefault(key, "Untagged");

                sharedResult.incrementTagCount(tag);
                sharedResult.incrementPortProtocolCount(dstport, protocol);
            }
        }, executor);
    }
}

