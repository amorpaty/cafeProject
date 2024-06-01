package com.cafe.cafeproject.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MainController {

    @Value("${kakao.apiKey}")
    private String kakaoApiKey;

    @Value("${kakao.redirectUri}")
    private String redirectUri;

    @GetMapping("/main")
    public ModelAndView mainView(@RequestParam HashMap<String, Object> params){
        ModelAndView mv = new ModelAndView();

        mv.addObject("apiKey", kakaoApiKey);
        mv.addObject("redirectUri", redirectUri);
        mv.addObject("nickname", params.get("nickname"));
        mv.addObject("email", params.get("email"));
        mv.setViewName("index.html");
        return mv;
    }
}
