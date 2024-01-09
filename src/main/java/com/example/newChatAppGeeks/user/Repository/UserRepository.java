package com.example.newChatAppGeeks.user.Repository;

import com.example.newChatAppGeeks.user.Model.User;
import com.example.newChatAppGeeks.user.Enum.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends MongoRepository<User, String> {
    List<User> findAllByStatus(Status status);
    Optional<User> findByNickNameAndPassword(String nickName,String password);

    Optional<User> findByNickNameAndEmail(String nickName,String email);
}
