package main.processor.data;

import java.util.concurrent.ConcurrentHashMap;

public class ProcessingResult {

    private final ConcurrentHashMap<String, Integer> tagCounts;
    private final ConcurrentHashMap<String, Integer> portProtocolCounts;

    public ProcessingResult() {
        this.tagCounts = new ConcurrentHashMap<>();
        this.portProtocolCounts = new ConcurrentHashMap<>();
    }

    public void incrementTagCount(String tag) {
        tagCounts.merge(tag, 1, Integer::sum);
    }

    public void incrementPortProtocolCount(String port, String protocol) {
        String key = port + ":" + protocol;
        portProtocolCounts.merge(key, 1, Integer::sum);
    }

    public ConcurrentHashMap<String, Integer> getTagCounts() {
        return tagCounts;
    }

    public ConcurrentHashMap<String, Integer> getPortProtocolCounts() {
        return portProtocolCounts;
    }
}

