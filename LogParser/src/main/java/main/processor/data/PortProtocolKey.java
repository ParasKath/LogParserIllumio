package main.processor.data;

import java.util.Objects;

public class PortProtocolKey {
    private final String dstport;
    private final String protocol;

    public PortProtocolKey(String dstport, String protocol) {
        this.dstport = dstport;
        this.protocol = protocol.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortProtocolKey that = (PortProtocolKey) o;
        return dstport.equals(that.dstport) && protocol.equals(that.protocol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dstport, protocol);
    }
}
