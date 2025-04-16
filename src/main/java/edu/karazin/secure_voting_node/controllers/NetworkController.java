package edu.karazin.secure_voting_node.controllers;

import edu.karazin.secure_voting_node.dto.NodeAddressDto;
import edu.karazin.secure_voting_node.services.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/network")
public class NetworkController {
    private final NetworkService networkService;

    @GetMapping("/connect")
    public Flux<NodeAddressDto> connectToNetwork(@RequestParam String host) {
        return networkService.fetchRandomNodes(host);
    }
}
