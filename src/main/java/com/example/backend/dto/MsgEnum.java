package com.example.backend.dto;

public enum MsgEnum {
    dupleEmail("중복된 이메일 입니다."),
    emailTitle("이메일 인증"),
    emailContentFront("인증번호: "),
    emailContentEnd(""),
    emailSend("인증 메일을 전송 하였습니다."),
    incorrectEmailCode("인증에 실패하였습니다."),
    correctEmailCode("인증에 성공하였습니다."),
    dupleNick("중복된 닉네임 입니다."),
    availableNick("사용 가능한 닉네임 입니다."),
    registerSuccess("회원가입 완료 하였습니다.");

    final private String msg;
    public String getMsg() {
        return msg;
    }
    private MsgEnum(String msg){
        this.msg = msg;
    }
}
