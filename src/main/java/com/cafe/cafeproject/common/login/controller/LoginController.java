package com.cafe.cafeproject.common.login.controller;

import com.cafe.cafeproject.common.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;

@Controller(value = "/")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

//    @Value("kakao.apiKey")
//    private String kakaoApiKey;
//
//    @Value("kakao.redirectUri")
//    private String redirectUri;

//    @Value("${google.client.id}")
//    private String googleClientId;
//
//    @Value("${google.client.pw}")
//    private String googleClientPw;

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

    /**
     * 카카오 로그인 API 연동
     * @param code
     * @return
     */
    @GetMapping("/login/kakao-login")
    public ModelAndView kakaoLogin(@RequestParam String code){
        ModelAndView mv = new ModelAndView();
        String resultUrl = "/";
        // 2. 토큰 받기
        String accessToken = loginService.getAccessToken(code);

        // kakao 사용자 정보 조회 (닉네임, 이메일)
        HashMap<String, Object> userInfo = loginService.getUserInfo(accessToken);

        if(CollectionUtils.isEmpty(userInfo)){
            resultUrl = "redirect:/";
        }else{
            resultUrl = "redirect:/main";
            mv.addObject("userInfo", userInfo);
        }
        mv.setViewName(resultUrl);
        return mv;
    }

    /**
     * 구글 로그인 API 연동
     * @return
     */
    @GetMapping("/login/google-login")
    public ModelAndView goolgleLogin(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/");
        return mav;
    }
}
