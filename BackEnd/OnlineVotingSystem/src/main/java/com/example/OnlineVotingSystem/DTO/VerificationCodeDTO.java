package com.example.OnlineVotingSystem.DTO;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class VerificationCodeDTO {
    @Email
    private String email;
    private String inputtedCode;
}
