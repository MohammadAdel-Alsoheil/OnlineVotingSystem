package com.example.OnlineVotingSystem.repo;

import com.example.OnlineVotingSystem.models.VerificationCodeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface VerificationCodeRepo extends MongoRepository<VerificationCodeModel, String> {
    VerificationCodeModel findByEmail(String email);
    void deleteById(String id);

}
