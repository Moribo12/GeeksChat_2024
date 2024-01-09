package com.example.newChatAppGeeks.user.Service;

import com.example.newChatAppGeeks.user.Model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    public User registerUser(String nickName, String password, String email);
     public User authenticate(String nickName, String password);
     ResponseEntity<List<User>> findConnectedUsers();
     public void disconnect(String nickName);
}
