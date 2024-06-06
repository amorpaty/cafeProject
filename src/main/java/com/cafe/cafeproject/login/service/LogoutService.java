package com.cafe.cafeproject.login.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
public class LogoutService {

    @Value("${kakao.apiKey}")
    private String kakaoApiKey;

    @Value("${kakao.redirectUri}")
    private String redirectUri;

    /**
     * 로그인 연결 끊기
     * @param uri
     * @param session
     * @throws IOException
     */
    public void logout(String uri, HttpSession session) throws IOException {
        String accessToken = (String)session.getAttribute("accessToken");

        URL url = new URL("https://kapi.kakao.com/v1/user/logout");
        HttpURLConnection coon = (HttpURLConnection) url.openConnection();
        coon.setRequestMethod("POST");
        coon.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = coon.getResponseCode();

        if(responseCode !=200){
            throw new IOException("responseCode : " + coon.getResponseCode() + " / " + coon.getResponseMessage())    ;
        }

    }
}
