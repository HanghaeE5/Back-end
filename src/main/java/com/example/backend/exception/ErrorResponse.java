package com.example.backend.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// ApiExceptionHandler 에서 사용됨
@Getter
@Builder
public class ErrorResponse {

    private String msg;
    private String errorCode;
    private HttpStatus httpStatus;

    public static ResponseEntity<ErrorResponse> of(ErrorCode code) {
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(
                        ErrorResponse.builder()
                                .msg(code.getMsg())
                                .errorCode(code.getErrorCode())
                                .httpStatus(code.getHttpStatus())
                                .build()
                );
    }

}