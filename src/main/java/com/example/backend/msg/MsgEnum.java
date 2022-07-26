package com.example.backend.msg;

public enum MsgEnum {
    //USER
    EMAIL_TITLE("이메일 인증"),
    EMAIL_CONTENT_FRONT("인증번호: "),
    EMAIL_SEND("인증 메일을 전송 하였습니다."),
    CORRECT_EMAIL_CODE("인증에 성공하였습니다."),
    AVAILABLE_NICK("사용 가능한 닉네임 입니다."),
    REGISTER_SUCCESS("회원가입 완료 하였습니다."),
    JWT_HEADER_NAME("Authorization"),
    REFRESH_HEADER_NAME("Refresh"),
    LOGIN_SUCCESS("로그인 완료"),
    REISSUE_COMPLETED_TOKEN("토큰 재발급 완료"),
    AUTHENTICATION_FAIL("Authentication failed, login or reissue token"),
    SOCIAL_REGISTER_SUCCESS("소셜 회원가입 완료"),
    PASSWORD_UPDATE_SUCCESS("비밀번호 변경 완료"),

    EVENT_PHONE_SUCCESS("휴대폰 번호 입력 완료"),

    //BOARD
    BOARD_SAVE_SUCCESS("파일 업로드 및 게시글 작성 완료"),

    BOARD_DELETE_SUCCESS("게시글 삭제 완료"),

    BOARD_UPDATE_SUCCESS("게시글 수정 완료"),

    CHALLENGE_APPLY_SUCCESS("첼린지 신청 완료"),

    CHALLENGE_CANCEL_SUCCESS("첼린지 취소 완료"),

    IMAGE_UPLOAD_FAIL("이미지 업로드에 실패했습니다."),

    IMAGE_DOMAIN("https://ohnigabucket.s3.ap-northeast-2.amazonaws.com/"),

    //ToDo목록
    TODO_SAVE_SUCCESS("Todo 추가 완료"),
    TODO_DONE("Todo 완료"),
    TODO_UPDATE_SUCCESS("Todo 수정 완료"),
    TODO_DELETE_SUCCESS("Todo 삭제 완료"),
    TODO_SCOPE_CHANGED("Todo 공개범위 변경 완료"),

    // 캐릭터
    CHARACTER_SELECTED("캐릭터를 선택했습니다"),
    TURTLE_ONE_URL("https://ohnigabucket.s3.ap-northeast-2.amazonaws.com/gif/turtle1.gif"),
    TURTLE_TWO_URL("https://ohnigabucket.s3.ap-northeast-2.amazonaws.com/gif/turtle2.gif"),
    TURTLE_THIRD_URL("https://ohnigabucket.s3.ap-northeast-2.amazonaws.com/gif/turtle3.gif"),
    SLOTH_ONE_URL("https://ohnigabucket.s3.ap-northeast-2.amazonaws.com/gif/sloth1.gif"),
    SLOTH_TWO_URL("https://ohnigabucket.s3.ap-northeast-2.amazonaws.com/gif/sloth2.gif"),
    SLOTH_THIRD_URL("https://ohnigabucket.s3.ap-northeast-2.amazonaws.com/gif/sloth3.gif"),

    TURTLE_ONE_NAME("등껍질은 비니 침대"),
    TURTLE_TWO_NAME("달팽이보단 빠른 비니"),
    TURTLE_THIRD_NAME(" 출격 완료 ! 비니"),
    SLOTH_ONE_NAME("이불 밖은 무서운 브라우니"),
    SLOTH_TWO_NAME("3종 오토 브라우니"),
    SLOTH_THIRD_NAME("현자 브라우니"),


    // 채팅
    CHAT_ROOM_EXIT("채팅방에서 퇴장했습니다"),
    CHAT_ROOM_ENTER("채팅방에 입장했습니다"),
    COUNT_PARTICIPANT("COUNT_PARTICIPANT"),
    SESSION_PARTICIPANT_MAPPING("SESSION_PARTICIPANT_MAPPING"),

    ;


    final private String msg;
    public String getMsg() {
        return msg;
    }
    private MsgEnum(String msg){
        this.msg = msg;
    }
}
