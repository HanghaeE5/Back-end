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

    INVALID_OLD_PWD(HttpStatus.BAD_REQUEST, "400", "기존 비밀번호가 옳바르지 않습니다."),

    NOT_LOGIN(HttpStatus.BAD_REQUEST, "400", "로그인이 필요합니다."),

    NOT_EXPIRED_TOKEN_YET(HttpStatus.BAD_REQUEST,"400", "토큰이 만료되지 않았습니다."),

    NEED_NICK(HttpStatus.BAD_REQUEST, "400", "닉네임 입력 후 서비스 이용 가능합니다."),

    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"400","유효하지 않은 리프레시 토큰입니다."),

    NEED_EMAIL(HttpStatus.BAD_REQUEST,"400","이메일은 필수로 동의 해주셔야 합니다."),

    SOCIAL_NOT_UPDATE_PASSWORD(HttpStatus.BAD_REQUEST,"400","소셜 회원가입한 사용자는 비밀번호 변경할 수 없습니다."),

    // Todo목록
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 목록이 존재하지 않습니다"),
    INCORRECT_USERID(HttpStatus.FORBIDDEN, "403", "목록의 작성자가 아닙니다"),

    // 게시글
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 게시글이 존재하지 않습니다"),

    // 채팅
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 채팅방이 존재하지 않습니다"),


    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "이미지를 입력해주세요."),
    INVALID_CHALLENGE(HttpStatus.BAD_REQUEST, "400", "챌린저스 카테고리와 Todo 내용을 확인해주세요."),

    // 친구
    EXISTING_REQUEST(HttpStatus.BAD_REQUEST, "400", "이미 친구추가를 보낸 사용자 입니다"),
    EXISTING_FRIEND(HttpStatus.BAD_REQUEST, "400", "친구입니다");

    // 추후 추가 코드

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String msg;

}
