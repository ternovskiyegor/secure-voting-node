package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.NodeAddressDto;
import edu.karazin.secure_voting_node.utils.DnsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NetworkService {
    private final WebClient.Builder webClientBuilder;
    private final DnsUtils dnsUtils;

    public Flux<NodeAddressDto> fetchRandomNodes(String host) {
        try {
            String ip = dnsUtils.getCatalogServer(host);
            log.info("Resolved catalog IP: {}", ip);
            String url = "http://" + ip + ":8080/api/nodes/random";
            return webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(NodeAddressDto.class);
        } catch (UnknownHostException e) {
            log.debug("DNS resolution or API request failed");
            return Flux.empty();
        }
    }
}
