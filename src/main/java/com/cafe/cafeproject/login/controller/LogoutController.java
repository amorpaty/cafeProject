package com.cafe.cafeproject.login.controller;

import com.cafe.cafeproject.common.storage.SesstionStorage;
import com.cafe.cafeproject.login.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutService logoutService;

    private final SesstionStorage sesstionStorage;

    @Value("${kakao.logoutUri}")
    private String kakaoLogoutUri;

    /**
     * 카카오 로그인 API 연동
     * @return ModelAndView
     */
    @GetMapping
    public ModelAndView commonLogout(HttpServletRequest request) throws Exception{

        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession();

        if(!ObjectUtils.isEmpty(session.getAttribute("accessToken"))) {
            String logoutUri = kakaoLogoutUri;
            String ssoType = (String) session.getAttribute("ssoType");

            if(ssoType.equalsIgnoreCase("KAKAO")){
                logoutUri = kakaoLogoutUri;
            }else if(ssoType.equalsIgnoreCase("GOOGLE")){
                //TODO
                //구글 로그아웃 URI 연동
                logoutUri = null;
            }else{
                throw new Exception();
            }
            logoutService.logout(logoutUri, session);
            sesstionStorage.destroySesstion(request);
        }

        mv.setViewName("redirect:/main");
        return mv;
    }
}
