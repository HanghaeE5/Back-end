package com.example.backend.chat.controller;

import com.example.backend.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mvc")
public class MvcController {
    private final ChatRoomRepository repo;

    @GetMapping("/login")
    public String loginMvc() {
        return "login";
    }

    @GetMapping("/room/list")
    public ModelAndView roomList() {
        System.out.println("Model And View Controller");

        ModelAndView mv = new ModelAndView("login");
        mv.addObject("list", repo.findAll());

        return mv;
    }

    @GetMapping("/room/enter/{id}")
    public void room(@PathVariable String id, Model model) {
        model.addAttribute("login", repo.findById(id));
    }

}
