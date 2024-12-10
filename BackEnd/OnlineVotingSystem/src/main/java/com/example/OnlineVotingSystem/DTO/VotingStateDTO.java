package com.example.OnlineVotingSystem.DTO;


import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class VotingStateDTO {
    private boolean votingState;
    private String email;

}