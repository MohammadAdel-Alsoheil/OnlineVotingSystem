package com.example.OnlineVotingSystem.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserDTO {

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
}
