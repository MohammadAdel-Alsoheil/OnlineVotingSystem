package com.example.OnlineVotingSystem.repo;


import com.example.OnlineVotingSystem.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepo extends MongoRepository<User,String> {

    @Query("{email: ?0, password:?1}")
    Optional<User> getByEmailAndPassword(String email, String password);


   @Query("{email: ?1}")
   @Update("{ $set: {votingState: ?0 }}")
   void changeVotingState(boolean votingState, String email);

   @Query("{isCandidate: ?0}")
    List<User> getByCandidateState(Boolean votingState);
}
