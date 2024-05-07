package com.cafe.cafeproject.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/main")
    public ModelAndView mainView(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/main/main.html");
        return mv;

    }
}
