package com.example.backend.character.controller;

import com.example.backend.character.domain.Type;
import com.example.backend.character.service.CharacterService;
import com.example.backend.msg.MsgEnum;
import com.example.backend.user.common.LoadUser;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/character")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @ApiOperation(value = "캐릭터 초기 설정")
    @PostMapping("/select")
    public ResponseEntity<String> selectCharacter(
            @RequestBody Type type
    ) {
        LoadUser.loginAndNickCheck();
        characterService.selectCharacter(LoadUser.getEmail(), type);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.CHARACTER_SELECTED.getMsg());
    }
}
