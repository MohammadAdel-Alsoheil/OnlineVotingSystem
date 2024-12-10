package com.example.OnlineVotingSystem.utils;

import org.springframework.stereotype.Component;

@Component
public class Utils {


    public boolean checkPasswordStrength(String password){
        if(password.length()<8){
            return false;
        }
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for(int i = 0;i<password.length();++i){
            char ch = password.charAt(i);

            if(Character.isUpperCase(ch)){
                hasUpperCase = true;
            }
            else if(Character.isLowerCase(ch)){
                hasLowerCase = true;
            }
            else if(Character.isDigit(ch)){
                hasDigit = true;
            }
            else if(!Character.isLetterOrDigit(ch)){
                hasSpecial = true;
            }

            if(hasDigit&&hasSpecial&&hasLowerCase&&hasUpperCase){
                return true;
            }
        }
        return false;
    }
}
