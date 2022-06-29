package com.example.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //RestController의 예외처리에 대해서 AOP를 적용하기 위해 사용
public class ApiException extends RuntimeException{

    @ExceptionHandler(MethodArgumentNotValidException.class) //예외가 발생한 요청을 처리하기 위해
    public ResponseEntity<String> handleMethodNotValidException(MethodArgumentNotValidException e){
//        Map<String, String> errors = new HashMap<>();
        final String[] message = {""};

        e.getBindingResult().getAllErrors()
                .forEach(c -> message[0] = c.getDefaultMessage().toString());

        return ResponseEntity.badRequest().body(message[0]);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleArgumentException(IllegalArgumentException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<String> mailException(MailException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
