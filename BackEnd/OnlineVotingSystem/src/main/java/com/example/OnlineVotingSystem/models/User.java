package com.example.OnlineVotingSystem.models;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
public class User {
    @Id
    private String ID;
    @NotEmpty(message = "User must have a name")
    private String name;
    @NotEmpty
    @Email
    private String email;
    @Min(18)
    @NotEmpty(message = "User must have an age")
    private String age;
    private String password;
    private String party;
    private Boolean isCandidate;
    private String prKey;
    private String pbKey;
    private Boolean votingState;
}
