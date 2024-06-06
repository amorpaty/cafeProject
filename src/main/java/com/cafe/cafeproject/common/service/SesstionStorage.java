package com.cafe.cafeproject.common.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Map;


@Component
public class SesstionStorage {

    /**
     * 로그인 세션 생성
     * @param accessToken
     * @param userInfo
     * @param request
     * @return
     */
    public HttpSession getSesstion(String ssoType,  String accessToken, Map<String, Object> userInfo, HttpServletRequest request){
        HttpSession session = request.getSession();
        // 2. 세션에 회원 정보 저장 & 세션 유지 시간 설정
        if (!CollectionUtils.isEmpty(userInfo)) {
            session.setAttribute("ssoType", ssoType);
            session.setAttribute("accessToken", accessToken);
            session.setAttribute("userInfo", userInfo);
            session.setMaxInactiveInterval(600 * 30);
        }
        return session;
    }

    /**
     * 로그인 세션 제거
     * @param request
     */
    public void destroySesstion(HttpServletRequest request){
        HttpSession session = request.getSession();

        if(!ObjectUtils.isEmpty(session)){
            session.invalidate();
        }
    }
}
