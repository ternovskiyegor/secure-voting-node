package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.NodeAddressDto;
import edu.karazin.secure_voting_node.dto.NodeMessageWrapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NodeConnectionService {
    private final WebClient.Builder webClientBuilder;
    private final NodeService nodeService;

    @Value("${node.connections.limit}")
    private int connectionLimit;

    private final Map<String, WebClient> activeConnections = new ConcurrentHashMap<>();

    @PostConstruct
    public void initializeConnections() {
        try {
            List<NodeAddressDto> nodes = nodeService.getAllNodes();

            nodes.stream()
                    .limit(connectionLimit)
                    .forEach(this::connectToNode);
        } catch (Exception e) {
            log.error("Error during initial node connection setup: {}", e.getMessage());
        }
    }

    public void connectToNode(NodeAddressDto nodeDto) {
        String baseUrl = "http://" + nodeDto.getIp() + ":" + nodeDto.getPort();
        WebClient client = webClientBuilder.baseUrl(baseUrl).build();

        client.get().uri("/ping")
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> {
                    log.info("Connected to node: {}", baseUrl);
                    activeConnections.put(baseUrl, client);
                })
                .doOnError(error -> log.warn("Failed to connect to node: {}", baseUrl))
                .subscribe();
    }

    @Scheduled(fixedDelayString = "${node.connections.refresh-limit}")
    public void refreshConnection() {
        log.info("Refreshing node connections");
        Set<String> failedConnections = new HashSet<>();

        // checking all active connections
        activeConnections.forEach((url, client) -> {
            client.get().uri("/ping")
                    .retrieve()
                    .toBodilessEntity()
                    .doOnError(err -> {
                        log.warn("Node down: {}", url);
                        failedConnections.add(url);
                    })
                    .subscribe();
        });

        failedConnections.forEach(activeConnections::remove);

        int needLimit = connectionLimit - activeConnections.size();
        if (needLimit > 0) {
            nodeService.getAllNodes().stream()
                    .filter(node -> !activeConnections.containsKey("http://" + node.getIp() + ":" + node.getPort()))
                    .limit(needLimit)
                    .forEach(this::connectToNode);
        }
    }

    public Collection<WebClient> getActiveConnections() {
        return activeConnections.values();
    }

    public void broadcastToNode(NodeMessageWrapper messageWrapper) {
        activeConnections.forEach((url, client) -> {
            client.post()
                    .uri("/api/node/message")
                    .bodyValue(messageWrapper)
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(resp -> log.info("Successfully sent message to {}", url))
                    .doOnError(err -> log.warn("Failed to send message to {} : {}", url, err.getMessage()))
                    .subscribe();
        });
    }

}
