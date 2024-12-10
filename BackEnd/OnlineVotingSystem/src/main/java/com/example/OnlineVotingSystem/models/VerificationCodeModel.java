package com.example.OnlineVotingSystem.models;


import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Document(collection = "verification_codes")
public class VerificationCodeModel {

    @Id
    private String Id;
    @Email
    private String email;
    private String code;
    private LocalDateTime expiresAt;
}
