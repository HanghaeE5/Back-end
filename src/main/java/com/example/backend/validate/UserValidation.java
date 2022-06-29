package com.example.backend.validate;
import com.example.backend.msg.MsgEnum;
import com.example.backend.user.security.UserDetailsImpl;

public class UserValidation {
    public static void loginCheck(UserDetailsImpl userDetails) {
        if(userDetails == null){
            throw new IllegalArgumentException(MsgEnum.loginRequired.getMsg());
        }
    }

    //로그인되어있는지와 닉네임이 등록되어있는지 둘다 확인해야합나다.
    public static void loginAndNickCheck(UserDetailsImpl userDetails) {
        loginCheck(userDetails);
        if(userDetails.getNick() == null){
            throw new IllegalArgumentException(MsgEnum.nicknameRequired.getMsg());
        }
    }
}
