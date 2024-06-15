package com.cafe.cafeproject.login.controller;

import com.cafe.cafeproject.common.storage.SesstionStorage;
import com.cafe.cafeproject.login.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    private final SesstionStorage sesstionStorage;

    /**
     * 카카오 로그인 API 연동
     * @param code
     * @return
     */
    @GetMapping("/kakao-login")
    public ModelAndView kakaoLogin(@RequestParam String code, HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        String resultUrl = "redirect:/main";

        // 2. 토큰 받기
        String accessToken = loginService.getAccessToken(code);

        // kakao 사용자 정보 조회 (닉네임, 썸네일, 이메일)
        HashMap<String, Object> userInfo = loginService.getUserInfo(accessToken);

        //로그인 세션 설정
        sesstionStorage.getSesstion("KAKAO", accessToken, userInfo, request);

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
