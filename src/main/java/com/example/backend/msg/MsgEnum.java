package com.example.backend.msg;

public enum MsgEnum {

    EMAIL_TITLE("이메일 인증"),
    EMAIL_CONTENT_FRONT("인증번호: "),
    EMAIL_SEND("인증 메일을 전송 하였습니다."),
    CORRECT_EMAIL_CODE("인증에 성공하였습니다."),
    AVAILABLE_NICK("사용 가능한 닉네임 입니다."),
    REGISTER_SUCCESS("회원가입 완료 하였습니다."),
    JWT_HEADER_NAME("Authorization"),
    LOGIN_SUCCESS("로그인 완료"),
    LOGIN_REQUIRED("로그인이 필요합니다"),
    NICKNAME_REQUIRED("닉네임을 작성해야 합니다.");


    final private String msg;
    public String getMsg() {
        return msg;
    }
    private MsgEnum(String msg){
        this.msg = msg;
    }
}
