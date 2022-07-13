package com.example.backend.event.controller;

import com.example.backend.event.dto.EventRequestDto;
import com.example.backend.event.dto.EventResponseDto;
import com.example.backend.event.dto.ProductResponseDto;
import com.example.backend.event.service.EventService;
import com.example.backend.user.common.LoadUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @ApiOperation(value = "이벤트 정보 조회하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
    })
    @GetMapping("/event")
    public ResponseEntity<EventResponseDto> getEventInfo(){
        LoadUser.loginAndNickCheck();
        return ResponseEntity.ok()
                .body(eventService.getEventInfo(LoadUser.getEmail()));
    }

    @ApiOperation(value = "도장을 응모권으로 교환하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
    })
    @PutMapping("/event/exchange")
    public ResponseEntity<EventResponseDto> stampToCoupon(){
        LoadUser.loginAndNickCheck();
        return ResponseEntity.ok()
                .body(eventService.stampToCoupon(LoadUser.getEmail()));
    }

    @ApiOperation(value = "응모권으로 럭키박스 열기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
    })
    @PutMapping("/event/open-box")
    public ResponseEntity<ProductResponseDto> openLuckyBox(){
        LoadUser.loginAndNickCheck();
        return ResponseEntity.ok()
                .body(eventService.openLuckyBox(LoadUser.getEmail()));
    }

    @ApiOperation(value = "당첨 후 휴대폰 번호 입력")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
    })
    @PutMapping("/event/phone")
    public ResponseEntity<String> eventPhoneRegister(
            @Valid  @RequestBody EventRequestDto requestDto
            ){
        LoadUser.loginAndNickCheck();
        return ResponseEntity.ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(eventService.eventPhoneRegister(LoadUser.getEmail(), requestDto));
    }
}
