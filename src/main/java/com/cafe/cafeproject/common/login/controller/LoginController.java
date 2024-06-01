package com.cafe.cafeproject.common.login.controller;

import com.cafe.cafeproject.common.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /**
     * 카카오 로그인 API 연동
     * @param code
     * @return
     */
    @GetMapping("/kakao-login")
    public ModelAndView kakaoLogin(@RequestParam String code){
        ModelAndView mv = new ModelAndView();
        String resultUrl = "/";
        // 2. 토큰 받기
        String accessToken = loginService.getAccessToken(code);

        // kakao 사용자 정보 조회 (닉네임, 이메일)
        HashMap<String, Object> userInfo = loginService.getUserInfo(accessToken);

        resultUrl = "redirect:/main";

        mv.addObject("userInfo", userInfo);
        mv.setViewName(resultUrl);

        return mv;
    }

    /**
     * 구글 로그인 API 연동
     * @return
     */
    @GetMapping("/google-login")
    public ModelAndView goolgleLogin(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/");
        return mav;
    }
}
