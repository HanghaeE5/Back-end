package com.example.backend.msg;

public enum MsgEnum {

    EMAIL_TITLE("이메일 인증"),
    EMAIL_CONTENT_FRONT("인증번호: "),
    EMAIL_SEND("인증 메일을 전송 하였습니다."),
    CORRECT_EMAIL_CODE("인증에 성공하였습니다."),
    AVAILABLE_NICK("사용 가능한 닉네임 입니다."),
    REGISTER_SUCCESS("회원가입 완료 하였습니다."),
    JWT_HEADER_NAME("Authorization"),
    REFRESH_HEADER_NAME("Refresh"),
    LOGIN_SUCCESS("로그인 완료"),
    LOGIN_REQUIRED("로그인이 필요합니다"),
    NICKNAME_REQUIRED("닉네임을 작성해야 합니다."),
    NOT_EXPIRED_TOKEN_YET("토큰이 만료되지 않았습니다."),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다."),
    REISSUE_COMPLETED_TOKEN("토큰 재발급 완료"),
    AUTHENTICATION_FAIL("Authentication failed, login or reissue token"),
    SOCIAL_REGISTER_SUCCESS("소셜 회원가입 완료");


    final private String msg;
    public String getMsg() {
        return msg;
    }
    private MsgEnum(String msg){
        this.msg = msg;
    }
}
