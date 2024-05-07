package com.cafe.cafeproject.common.login.controller;

import com.cafe.cafeproject.common.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

@Controller(value = "/")
@RestController
@RequiredArgsConstructor
public class LoginController {

    @Autowired
    private LoginService loginService;

//    @Value("kakao.apiKey")
//    private String kakaoApiKey;
//
//    @Value("kakao.redirectUri")
//    private String redirectUri;

    /**
     * 로그인 페이지 진입
     * @return
     */
    @GetMapping
    public ModelAndView loginView(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("apiKey", "ea5a3b253070cb52c63addbaf2bc2053");
        mv.addObject("redirectUri", "http://127.0.0.1:8080/login/kakao-login");
        mv.setViewName("index.html");
        return mv;
    }


    @GetMapping("/login/kakao-login")
    public ModelAndView loginUser(@RequestParam String code){
        ModelAndView mv = new ModelAndView();
        String resultUrl = "/";
        // 2. 토큰 받기
        String accessToken = loginService.getAccessToken(code);

        if(StringUtils.isEmpty(accessToken)){
            resultUrl = "redirect:/";
        }else{
            resultUrl = "redirect:/main";
        }
        mv.setViewName(resultUrl);
        return mv;
    }
}
