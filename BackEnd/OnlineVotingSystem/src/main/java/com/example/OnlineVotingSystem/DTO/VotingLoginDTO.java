package com.example.OnlineVotingSystem.DTO;


import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class VotingLoginDTO {
    @Email
    private String email;
    private String password;
}
