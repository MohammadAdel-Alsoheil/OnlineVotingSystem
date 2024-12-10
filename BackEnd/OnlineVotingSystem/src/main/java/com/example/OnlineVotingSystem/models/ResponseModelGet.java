package com.example.OnlineVotingSystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class ResponseModelGet {


    private String statusCode;
    private String statusMsg;
    private List<?> data;


}
