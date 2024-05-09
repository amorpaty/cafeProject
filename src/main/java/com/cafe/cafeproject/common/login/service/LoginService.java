package com.cafe.cafeproject.common.login.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class LoginService {

    @Value("${kakao.apiKey}")
    private String kakaoApiKey;

    @Value("${kakao.redirectUri}")
    private String redirectUri;

    public String getAccessToken(String code){
        String token = null;
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try {
            RestTemplate rt = new RestTemplate();
            //HttpHeader 오브젝트
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoApiKey);
            params.add("redirect_uri", redirectUri);
            params.add("code", code);

            //http 바디(params)와 http 헤더(headers)를 가진 엔티티
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                    new HttpEntity<>(params, headers);

            //reqUrl로 Http 요청 , POST 방식
            ResponseEntity<String> response =
                    rt.exchange(reqUrl, HttpMethod.POST, kakaoTokenRequest, String.class);

            String responseBody = response.getBody();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(responseBody);

            token = String.valueOf(jsonObject.get("access_token"));

        } catch (Exception e){
            e.printStackTrace();
        }

        return token;
    }
}
