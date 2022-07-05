package com.example.backend.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mvc")
public class MvcController {

    @GetMapping("/login")
    public String loginMvc() {
        return "/login";
    }

    @GetMapping("/room/list")
    public String roomList() {
        return "/chat/roomList";
    }

    @GetMapping("/room/enter/{id}")
    public String room(@PathVariable String id, Model model) {
        model.addAttribute("roomId", id);
        return "/chat/roomDetail";
    }

}
