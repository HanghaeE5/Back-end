package com.example.backend.user.common;

import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
public class LoadUser {
    public static String getEmail(){
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();
    }

    public static String getNick(){
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getPassword();
    }

    public static void loginCheck(){
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null){
            throw new CustomException(ErrorCode.NOT_LOGIN);
        }
    }

    public static void loginAndNickCheck(){
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null){
            throw new CustomException(ErrorCode.NOT_LOGIN);
        }
        if (principal.getPassword().isEmpty()){
            throw new CustomException(ErrorCode.NEED_NICK);
        }
    }
}
