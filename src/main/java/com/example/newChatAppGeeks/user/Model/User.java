package com.example.newChatAppGeeks.user.Model;

import com.example.newChatAppGeeks.user.Enum.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {
    @Id
    private String nickName;
//    private String fullName;
    private Status status;
    private String email;
    private String password;
}
