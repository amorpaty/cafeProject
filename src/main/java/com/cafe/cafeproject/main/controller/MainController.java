package com.cafe.cafeproject.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/main")
    public ModelAndView mainView(@RequestParam HashMap<String, Object> params){
        ModelAndView mv = new ModelAndView();
        mv.addObject("nickname", params.get("nickname"));
        //mv.addObject("email", params.get("email"));
        mv.setViewName("/main/main.html");
        return mv;
    }
}
