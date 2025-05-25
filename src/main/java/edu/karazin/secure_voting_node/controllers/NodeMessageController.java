package edu.karazin.secure_voting_node.controllers;

import edu.karazin.secure_voting_node.dto.NodeMessageWrapper;
import edu.karazin.secure_voting_node.services.NodeMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/node")
@RequiredArgsConstructor
public class NodeMessageController {

    private final NodeMessageService nodeMessageService;

    @PostMapping("/message")
    public ResponseEntity<?> receiveMessage(@RequestBody NodeMessageWrapper messageWrapper) {
        nodeMessageService.handleMessage(messageWrapper);
        return ResponseEntity.ok().build();
    }
}
