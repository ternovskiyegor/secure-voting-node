package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.NodeAddressDto;
import edu.karazin.secure_voting_node.models.Node;
import edu.karazin.secure_voting_node.repositories.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NodeService {
    private final NodeRepository nodeRepository;

    public void saveNode(List<NodeAddressDto> nodeAddressDtos) {
        List<Node> nodes = nodeAddressDtos.stream()
                .map(dto -> new Node(dto.getIp(), dto.getPort(), LocalDateTime.now())).collect(Collectors.toList());
        nodeRepository.deleteAll();
        nodeRepository.saveAll(nodes);
    }

    public List<NodeAddressDto> getAllNodes() {
        return nodeRepository.findAll().stream()
                .map(entity -> new NodeAddressDto(entity.getIp(), entity.getPort()))
                .collect(Collectors.toList());
    }

    public void clearAllNodes() {
        nodeRepository.deleteAll();
    }

}
