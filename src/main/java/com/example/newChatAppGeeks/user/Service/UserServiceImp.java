package com.example.newChatAppGeeks.user.Service;


import com.example.newChatAppGeeks.exception.LoginException;
import com.example.newChatAppGeeks.exception.RegisterException;
import com.example.newChatAppGeeks.user.Model.User;
import com.example.newChatAppGeeks.user.Enum.Status;
import com.example.newChatAppGeeks.user.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    // add ResponseObject for a descriptive error handling
    @Autowired
    UserRepository userRepository;

    public User registerUser(String nickName, String password, String email){
        if(nickName ==null || password ==null || email ==null){
            return null;
        }
        else{
            if(this.userRepository.findByNickNameAndEmail(nickName,email).isPresent()){
                throw new RegisterException("User already Exists!");

            }
            User user = new User();
            user.setNickName(nickName);
            user.setPassword(password);
            user.setStatus(Status.OFFLINE);
            user.setEmail(email);

            return this.userRepository.save(user);
        }
    }

    public User authenticate(String nickName, String password){
       Optional<User> userOptional = this.userRepository.findByNickNameAndPassword(nickName,password);

           if (userOptional.isPresent()) {
               User authenticatedUser = userOptional.get();
               authenticatedUser.setStatus(Status.ONLINE);
               this.userRepository.save(authenticatedUser);

               return authenticatedUser;
           }

           throw new LoginException("The user nickname or password is invalid");
    }


    public void disconnect(String nickName) {
        Optional<User> userOptional = this.userRepository.findById(nickName);

        if (userOptional.isPresent()) {
            User storedUser =userOptional.get();
            storedUser.setStatus(Status.OFFLINE);
            this.userRepository.save(storedUser);
        }
    }

    public ResponseEntity<List<User>> findConnectedUsers() {
        try {
            return new ResponseEntity<>(this.userRepository.findAllByStatus(Status.ONLINE),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }


}
