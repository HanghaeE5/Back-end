package com.example.backend.user.common;

import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
public class LoadUser {

    final private static User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    public static String getEmail(){
        return principal.getUsername();
    }

    public static String getNick(){
        return principal.getPassword();
    }

    public static void loginCheck(){
        if (principal == null){
            throw new CustomException(ErrorCode.NOT_LOGIN);
        }
    }

    public static void loginAndNickCheck(){
        if (principal == null){
            throw new CustomException(ErrorCode.NOT_LOGIN);
        }
        if (principal.getPassword().isEmpty()){
            throw new CustomException(ErrorCode.NEED_NICK);
        }
    }
}
