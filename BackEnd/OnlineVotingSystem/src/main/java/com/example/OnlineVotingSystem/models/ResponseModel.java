package com.example.OnlineVotingSystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseModel{
    private String statusCode;
    private String statusMsg;

}