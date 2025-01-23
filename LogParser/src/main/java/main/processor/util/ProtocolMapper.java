package main.processor.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProtocolMapper {

    private static final Map<String, String> PROTOCOL_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("1", "icmp");
        map.put("6", "tcp");
        map.put("17", "udp");
        map.put("41", "ipv6");
        map.put("50", "esp");
        map.put("51", "ah");
        map.put("58", "icmpv6");
        map.put("89", "ospf");
        map.put("132", "sctp");
        map.put("112", "vrrp");

        PROTOCOL_MAP = Collections.unmodifiableMap(map);
    }

    public static String mapProtocolNumber(String protocolNumber) {
        return PROTOCOL_MAP.get(protocolNumber);
    }
}

