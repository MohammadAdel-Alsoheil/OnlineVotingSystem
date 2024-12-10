package com.example.OnlineVotingSystem.service;


import com.example.OnlineVotingSystem.DTO.UserDTO;
import com.example.OnlineVotingSystem.DTO.VotingLoginDTO;
import com.example.OnlineVotingSystem.models.User;
import com.example.OnlineVotingSystem.models.VerificationCodeModel;
import com.example.OnlineVotingSystem.repo.UserRepo;
import com.example.OnlineVotingSystem.repo.VerificationCodeRepo;
import com.example.OnlineVotingSystem.resolvers.UserResolver;
import com.example.OnlineVotingSystem.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {


    private static final int CODELENGTH = 6;
    UserRepo userRepo;
    VerificationCodeRepo verificationCodeRepo;
    UserResolver userResolver;
    Utils utils;
    @Autowired
    HashingService hashingService;
    @Autowired
    EmailSenderService emailSenderService;

    public boolean createUser(UserDTO userDTO) {
        if (!utils.checkPasswordStrength(userDTO.getPassword())) {
            return false;
        }
        userRepo.save(userResolver.mapToUser(userDTO, new User()));
        return true;
    }

    public boolean Login(VotingLoginDTO votingLoginDTO) {
        Optional<User> Optuser;
        try {
            Optuser = userRepo.getByEmailAndPassword(votingLoginDTO.getEmail(), hashingService.hash(votingLoginDTO.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while logging in ", e);
        }
        if (Optuser.isPresent()) {
            User usr = Optuser.get();
            return usr.getVotingState();
        }
        return true; //equivalent to has voted, here no retrieved account
    }

    public void changeVotingState(boolean votingState, String email) {
        try {
            userRepo.changeVotingState(votingState, email);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update voting state for email: " + email, e);
        }

    }

    public void invokeVerificationRequest(String userEmail) {
        String code = "";
        Random rand = new Random();
        for (int i = 0; i < CODELENGTH; ++i) {
            int randomNumber = rand.nextInt(10); // Generates a random number between 0 and 9
            code += String.valueOf(randomNumber); // Append the number as a string
        }

        try {
            emailSenderService.sendEmail(userEmail,
                    "Voting Verification Code",
                    String.format("Dear Voter, do NOT share this code with anyone  (%S)%nIf this was not you, make sure to contact the company email.", code));

            VerificationCodeModel existingCode = verificationCodeRepo.findByEmail(userEmail);

            if (existingCode != null) {
                // If a code exists, update the existing document
                existingCode.setCode(hashingService.hash(code));
                existingCode.setExpiresAt(LocalDateTime.now().plusMinutes(1));
                verificationCodeRepo.save(existingCode);  // This will update the existing record
            } else {
                // If no code exists, create a new one
                verificationCodeRepo.save(new VerificationCodeModel(
                        null,
                        userEmail,
                        hashingService.hash(code),
                        LocalDateTime.now().plusMinutes(3)
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Can not send verification code to the User");
        }
    }

    public boolean verifyVerificationCodeRequest(String email, String code) {
        VerificationCodeModel codeObject = verificationCodeRepo.findByEmail(email);

        if (codeObject == null) {
            return false;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        if (codeObject.getExpiresAt().isBefore(currentTime)) {
            return false;
        }
        String generatedCode = codeObject.getCode();
        if(generatedCode.equals(hashingService.hash(code))){
            verificationCodeRepo.deleteById(codeObject.getId());
            return true;
        }
        return false;
    }

    public List<UserDTO> getCandidates(){
        List<User> candidatesM = userRepo.getByCandidateState(true);
        List<UserDTO> candidatesDTO = new ArrayList<>();
        for (User user:candidatesM ){
            candidatesDTO.add(userResolver.mapToUserDTO(user,new UserDTO()));
        }
        return candidatesDTO;
    }
}
