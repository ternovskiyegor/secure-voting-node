package edu.karazin.secure_voting_node.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class VoteResultDto {
    private Map<String, Long> results;
}
