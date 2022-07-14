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

    FILE_NULL(HttpStatus.BAD_REQUEST, "400", "파일을 입력해주세요."),

    STAMP_TABLE_NULL(HttpStatus.BAD_REQUEST, "400", "도장관련 정보가 없습니다."),

    STAMP_CNT_LESS(HttpStatus.BAD_REQUEST, "400", "도장 개수가 적습니다."),

    COUPON_NOT_FOUNT(HttpStatus.BAD_REQUEST, "400", "응모권이 없습니다"),

    WINNING_NOT_FOUNT(HttpStatus.BAD_REQUEST, "400", "당첨 기록이 없습니다."),

    // Todo목록
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 목록이 존재하지 않습니다"),
    INCORRECT_USERID(HttpStatus.FORBIDDEN, "403", "목록의 작성자가 아닙니다"),
    TODO_ALREADY_DONE(HttpStatus.BAD_REQUEST, "400", "이미 완료한 Todo 입니다"),

    // 게시글
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 게시글이 존재하지 않습니다"),
    CHALLENGE_NOT_DELETE(HttpStatus.BAD_REQUEST, "400", "첼린지는 삭제할 수 없습니다."),
    CHALLENGE_NOT_UPDATE(HttpStatus.BAD_REQUEST, "400", "첼린지는 수정 불가 합니다."),
    CHALLENGE_CANCEL_APPLY_NOT(HttpStatus.BAD_REQUEST, "400", "해당 챌린지는 마감되어 신청/취소할 수 없습니다."),
    CHALLENGE_CANCEL_AUTHOR_NOT(HttpStatus.BAD_REQUEST, "400", "첼린지 게시물을 작성한 사용자는 신청/취소할 수 없습니다."),

    DAILY_NOY_APPLY_CHALLENGE(HttpStatus.BAD_REQUEST, "400", "일상글은 참여/취소 할 수 없습니다."),

    NOT_APPLY_CHALLENGE_NOT_CANCEL(HttpStatus.BAD_REQUEST, "400", "신청 하지 않은 첼린지는 취소할 수 없습니다."),

    // 채팅
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 채팅방이 존재하지 않습니다"),
    EXISTING_ROOM(HttpStatus.BAD_REQUEST, "400", "상대와 이미 대화방이 존재합니다"),


    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "이미지를 입력해주세요."),
    INVALID_CHALLENGE(HttpStatus.BAD_REQUEST, "400", "챌린저스 카테고리와 Todo 내용을 확인해주세요."),

    // 친구
    EXISTING_REQUEST(HttpStatus.BAD_REQUEST, "400", "이미 친구추가를 보낸 사용자 입니다"),
    EXISTING_FRIEND(HttpStatus.BAD_REQUEST, "400", "친구입니다"),
    SELF_REQUEST(HttpStatus.BAD_REQUEST, "400", "자신에게 친구추가를 보낼 수 없습니다"),
    NO_FRIEND_REQUEST_TO(HttpStatus.NOT_FOUND, "404", "친구 요청을 보낸 적이 없습니다"),
    NO_FRIEND_REQUEST_FROM(HttpStatus.NOT_FOUND, "404", "친구 요청을 받은 적이 없습니다"),
    NOT_FRIEND(HttpStatus.BAD_REQUEST, "400", "친구가 아닙니다"),

    // 캐릭터
    CHARACTER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 캐릭터가 존재하지 않습니다"),


    // 정렬 옵션,
    INVALID_SORTING_OPTION(HttpStatus.BAD_REQUEST, "400", "잘못된 sort 옵션입니다"),
    INVALID_FILTER_OPTION(HttpStatus.BAD_REQUEST, "400", "잘못된 filter 옵션입니다"),

    ;

    // 추후 추가 코드

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String msg;

}
