package com.example.backend.msg;

public enum MsgEnum {
    dupleEmail("중복된 이메일 입니다."),
    emailTitle("이메일 인증"),
    emailContentFront("인증번호: "),
    emailContentEnd(""),
    emailSend("인증 메일을 전송 하였습니다."),
    incorrectEmailCode("이메일 인증에 실패하였습니다."),
    correctEmailCode("인증에 성공하였습니다."),
    dupleNick("중복된 닉네임 입니다."),
    availableNick("사용 가능한 닉네임 입니다."),
    registerSuccess("회원가입 완료 하였습니다."),
    socialRegisterSuccess("회원가입/로그인 완료 하였습니다."),
    jwtHeaderName("Authorization"),
    userNotFound("사용자를 찾을 수 없습니다."),
    loginSuccess("로그인 완료"),
    confirmEmailPwd("이메일 또는 비밀번호를 확인해주세요."),

    loginRequired("로그인이 필요합니다"),
    nicknameRequired("닉네임을 작성해야 합니다.");

    final private String msg;
    public String getMsg() {
        return msg;
    }
    private MsgEnum(String msg){
        this.msg = msg;
    }
}
