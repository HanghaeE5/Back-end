package com.example.backend.character.controller;

import com.example.backend.character.dto.CharacterRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/character")
public class CharacterController {

    @PostMapping("/done")
    public ResponseEntity<String> doneTodo(@RequestBody CharacterRequestDto requestDto) {

    }

}
