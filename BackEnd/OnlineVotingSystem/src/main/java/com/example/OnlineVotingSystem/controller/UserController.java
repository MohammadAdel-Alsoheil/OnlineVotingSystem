package com.example.OnlineVotingSystem.controller;


import com.example.OnlineVotingSystem.DTO.UserDTO;
import com.example.OnlineVotingSystem.DTO.VerificationCodeDTO;
import com.example.OnlineVotingSystem.DTO.VotingLoginDTO;
import com.example.OnlineVotingSystem.DTO.VotingStateDTO;
import com.example.OnlineVotingSystem.models.ResponseModel;
import com.example.OnlineVotingSystem.models.ResponseModelGet;
import com.example.OnlineVotingSystem.models.User;
import com.example.OnlineVotingSystem.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Validated
@AllArgsConstructor
public class UserController {
    UserService userService;

    // this endpoint is private and shall be added only by company admin manually
    @PostMapping(path = "/add")
    public ResponseEntity<ResponseModel> createUser(@RequestBody @Valid UserDTO userDTO) {
        System.out.println("User isCandidate: " + userDTO.getIsCandidate());
        if(!userService.createUser(userDTO)){
            return ResponseEntity
                    .status(400)
                    .body(new ResponseModel("400","Weak Password"));
        }
        return ResponseEntity
                .status(201)
                .body(new ResponseModel("201","User created successfully"));
    }

    @PostMapping(path = "/loginToVote")
    public ResponseEntity<ResponseModel> LoginUser(@RequestBody @Valid VotingLoginDTO votingLoginDTO){
        if(userService.Login(votingLoginDTO)){
            return ResponseEntity
                    .status(401)
                    .body(new ResponseModel("401","Login Failed Wrong credentials Or` You may have already voted"));
        }
        return ResponseEntity
                .status(200)
                .body(new ResponseModel("200", "Login is successful"));
    }
    @PostMapping(path = "/IVoted")
    public ResponseEntity<ResponseModel> changeVotingState(@RequestBody VotingStateDTO votingStateDTO){
        try {
            userService.changeVotingState(votingStateDTO.isVotingState(), votingStateDTO.getEmail());
            return ResponseEntity
                    .status(200)
                    .body(new ResponseModel("200", "You have Voted Successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(400)
                    .body(new ResponseModel("400", "Voting Failed: " + e.getMessage()));
        }
    }

    @PostMapping(path = "/getCode")
    // I used a map here instead of DTO because I am expecting a single input which is userEmail
    public ResponseEntity<ResponseModel> getVerificationCodeRequest(@RequestBody Map<String, String> payload){
        try {
            String userEmail = payload.get("userEmail");
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity
                        .status(400)
                        .body(new ResponseModel("400", "Email is required"));
            }
            userService.invokeVerificationRequest(userEmail);
        }catch (Exception e){
            return ResponseEntity
                    .status(400)
                    .body(new ResponseModel("400","Can not send Code"));
        }

        return ResponseEntity
                .status(200)
                .body(new ResponseModel("200","Code sent Successfully"));
    }

    @PostMapping(path = "/verifyCode")
    public ResponseEntity<ResponseModel> verifyVerificationCodeRequest(@RequestBody VerificationCodeDTO verificationCodeDTO){
        if(userService.verifyVerificationCodeRequest(verificationCodeDTO.getEmail(), verificationCodeDTO.getInputtedCode())){
            return ResponseEntity
                    .status(200)
                    .body(new ResponseModel("200","Code verified Successfully"));
        }
        return ResponseEntity
                .status(400)
                .body(new ResponseModel("400","Error verifying Code"));

    }

    @GetMapping(path = "/getCandidates")
    public ResponseEntity<List<UserDTO>> getCandidates(){
        List<UserDTO> candidates =  userService.getCandidates();
            return ResponseEntity
                    .status(200)
                    .body(candidates);

    }



}

