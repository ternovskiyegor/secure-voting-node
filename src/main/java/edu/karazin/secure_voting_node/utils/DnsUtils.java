package edu.karazin.secure_voting_node.utils;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class DnsUtils {
    public String getCatalogServer(String host) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName(host);
        return inetAddress.getHostAddress();
    }
}
