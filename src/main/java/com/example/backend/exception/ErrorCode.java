package com.example.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 회원가입 + 로그인 + 이메일 체크
    DUPLE_EMAIL(HttpStatus.BAD_REQUEST, "400", "중복된 이메일 입니다."),
    EMAIL_CONTENT_END(HttpStatus.BAD_REQUEST, "400", ""),
    INCORRECT_EMAIL_CODE(HttpStatus.BAD_REQUEST, "400", "이메일 인증에 실패하였습니다."),
    DUPLE_NICK(HttpStatus.BAD_REQUEST, "400", "중복된 닉네임 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "사용자를 찾을 수 없습니다."),
    CONFIRM_EMAIL_PWD(HttpStatus.BAD_REQUEST, "400", "이메일 또는 비밀번호를 확인해주세요."),

    // Todo목록
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 목록이 존재하지 않습니다"),
    INCORRECT_USERID(HttpStatus.FORBIDDEN, "403", "목록의 작성자가 아닙니다"),

    // 게시글
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 게시글이 존재하지 않습니다"),

    ;

    // 추후 추가 코드

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String msg;

}