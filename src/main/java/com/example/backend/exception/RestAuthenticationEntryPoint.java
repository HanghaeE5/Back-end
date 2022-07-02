package com.example.backend.exception;

import com.example.backend.msg.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream().println(
                "{ "+
                    "\"error\":\""+authException.getMessage()+"\"," +
                    "\"msg\":\""+ MsgEnum.AUTHENTICATION_FAIL.getMsg() +"\"" +
                " }"
        );
        log.info("Responding with unauthorized error. Message := {}", authException.getMessage());
        log.info("AUTHENTICATION_FAIL := {}", MsgEnum.AUTHENTICATION_FAIL.getMsg());
    }
}
