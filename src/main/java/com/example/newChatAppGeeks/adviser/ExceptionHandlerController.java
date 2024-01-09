package com.example.newChatAppGeeks.adviser;

import com.example.newChatAppGeeks.exception.LoginException;
import com.example.newChatAppGeeks.exception.RegisterException;
import com.example.newChatAppGeeks.user.Model.User;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginException.class)
    public String handleLoginException(Exception exc, Model model){
        model.addAttribute("loginRequest",new User());
        model.addAttribute("errMsg",exc.getMessage());

        return "login_page";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegisterException.class)
    public String handleRegisterException(Exception exc, Model model){
        model.addAttribute("registerRequest",new User());
        model.addAttribute("errMsg",exc.getMessage());

        return "register_page";
    }



}
