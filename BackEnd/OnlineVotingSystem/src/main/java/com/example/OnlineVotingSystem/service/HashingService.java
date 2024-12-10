package com.example.OnlineVotingSystem.service;

import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashingService {

    private static final String ALGORITHM = "SHA3-256";

    public String hash(String input) {
        try {
            // Create MessageDigest instance for SHA3-256
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);

            // Compute the hash
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert hash bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not supported: " + ALGORITHM, e);
        }
    }
}

