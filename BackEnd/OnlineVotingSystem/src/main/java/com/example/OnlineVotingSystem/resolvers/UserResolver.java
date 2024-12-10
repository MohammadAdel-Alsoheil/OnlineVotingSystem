package com.example.OnlineVotingSystem.resolvers;

import com.example.OnlineVotingSystem.DTO.UserDTO;
import com.example.OnlineVotingSystem.models.User;
import com.example.OnlineVotingSystem.service.HashingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserResolver {
    @Autowired
    HashingService hashingService;

    public  User mapToUser(UserDTO userDTO, User user){
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashingService.hash(userDTO.getPassword()));
        user.setParty(userDTO.getParty());
        user.setIsCandidate(userDTO.getIsCandidate());



        user.setVotingState(false);

        return user;
    }

    public UserDTO mapToUserDTO (User user, UserDTO userDTO){
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setEmail(user.getEmail());
        userDTO.setParty(user.getParty());
        userDTO.setIsCandidate(user.getIsCandidate());

        return userDTO;
    }

}




/*
public static CardsDto mapToCardsDto(Cards cards, CardsDto cardsDto) {
        cardsDto.setCardNumber(cards.getCardNumber());
        cardsDto.setCardType(cards.getCardType());
        cardsDto.setMobileNumber(cards.getMobileNumber());
        cardsDto.setTotalLimit(cards.getTotalLimit());
        cardsDto.setAvailableAmount(cards.getAvailableAmount());
        cardsDto.setAmountUsed(cards.getAmountUsed());
        return cardsDto;
    }

    public static Cards mapToCards(CardsDto cardsDto, Cards cards) {
        cards.setCardNumber(cardsDto.getCardNumber());
        cards.setCardType(cardsDto.getCardType());
        cards.setMobileNumber(cardsDto.getMobileNumber());
        cards.setTotalLimit(cardsDto.getTotalLimit());
        cards.setAvailableAmount(cardsDto.getAvailableAmount());
        cards.setAmountUsed(cardsDto.getAmountUsed());
        return cards;
    }
 */