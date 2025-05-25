package edu.karazin.secure_voting_node.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "node")
@Data
@NoArgsConstructor
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "port")
    private int port;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdate;

    @Column(name = "active")
    private boolean active;

    public Node(String ip, int port, LocalDateTime lastUpdate) {
        this.ip = ip;
        this.port = port;
        this.lastUpdate = lastUpdate;
    }
}
