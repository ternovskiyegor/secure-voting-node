package edu.karazin.secure_voting_node.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ballot")
@Data
public class Ballot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voter_id")
    private String voterId;

    @Column(name = "encrypted_vote")
    private String encryptedVote;

    @Column(name = "signature")
    private String signature;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
