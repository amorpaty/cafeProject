package com.cafe.cafeproject.common.config;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

/**
 * 쿠키 Config
 * @since 2024.06.20
 * @author 남은주
 */
@Configuration
public class CookieConfig {

    /**
     * 쿠키 생성
     * @param name
     * @param value
     * @param response
     */
    public void createCookie(String name, String value, HttpServletResponse response){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/"); //root에 저장
        cookie.setMaxAge(60 * 60 * 24); //하루동안 저장
        response.addCookie(cookie); //전송!
    }

    //쿠키 한개씩 삭제
    /**
     * 
     * @param name
     * @param request
     * @param response
     */
    public void  removeCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        // 지금은 cookies가 null이 아님.
        // 쿠키가 삭제할때는 setMaxAge를 0으로 설정해주면 사라진다.
        for (int i = cookies.length - 1; i >= 0; i--) {
            if (cookies[i].getName().equals(name)) {
                cookies[i].setPath("/");
                cookies[i].setMaxAge(0);
                response.addCookie(cookies[i]);
                break;
            }
        }
    }
}
