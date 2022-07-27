package com.example.backend.mail;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    void sendEmail(String email, String code);
}
