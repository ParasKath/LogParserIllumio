package main.processor;


import main.processor.io.OutputWriter;
import main.processor.io.LookupLoader;
import main.processor.data.ProcessingResult;
import main.processor.data.PortProtocolKey;
import main.processor.chunk.ChunkProcessor;
import main.processor.util.ChunkSizeCalculator;
import main.processor.util.LoadConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlowLogProcessor {

    public static void main(String[] args) throws Exception {

        Properties properties = LoadConfigProperties.loadProperties("src/main/resources/properties/config.properties");

        // Input files and output directory
        String flowLogFile = properties.getProperty("flowLogFile");
        String lookupFile = properties.getProperty("lookupFile");
        String outputDir = properties.getProperty("outputDir");
        final int THREAD_COUNT = Integer.parseInt(properties.getProperty("MaxThreadCount"));
        final int CHUNK_SIZE = ChunkSizeCalculator.calculateChunkSize(flowLogFile,properties,THREAD_COUNT);
        // Load lookup table
        Map<PortProtocolKey, String> lookupTable = LookupLoader.loadLookupTable(lookupFile);

        // Thread pool for parallel processing
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Shared processing result
        ProcessingResult sharedResult = new ProcessingResult();

        // Process flow logs
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(flowLogFile))) {
            List<String> chunk = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() >= CHUNK_SIZE) {
                    List<String> chunkCopy = new ArrayList<>(chunk);
                    futures.add(ChunkProcessor.processChunk(chunkCopy, lookupTable, sharedResult, executor));
                    chunk.clear();
                }
            }

            if (!chunk.isEmpty()) {
                List<String> chunkCopy = new ArrayList<>(chunk);
                futures.add(ChunkProcessor.processChunk(chunkCopy, lookupTable, sharedResult, executor));
            }

            // Wait for all tasks to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // Write output
            OutputWriter.writeTagCounts(sharedResult.getTagCounts(), outputDir + "/tag_counts.csv");
            OutputWriter.writePortProtocolCounts(sharedResult.getPortProtocolCounts(), outputDir + "/port_protocol_counts.csv");
            System.out.println("Processing completed successfully!");
        } finally {
            executor.shutdown();
        }
    }
}

