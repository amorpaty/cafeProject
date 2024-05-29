package com.cafe.cafeproject.common.login.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


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
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

            //reqUrl로 Http 요청 , POST 방식
            ResponseEntity<String> response = rt.exchange(reqUrl, HttpMethod.POST, kakaoTokenRequest, String.class);

            String responseBody = response.getBody();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(responseBody);

            token = String.valueOf(jsonObject.get("access_token"));

        } catch (Exception e){
            e.printStackTrace();
        }

        return token;
    }

    public HashMap<String, Object> getUserInfo(String accessToken){
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(result);

            JSONObject kakao_account = (JSONObject) jsonObject.get("kakao_account");
            JSONObject profile = (JSONObject) kakao_account.get("profile");

            String nickname = profile.get("nickname").toString();
            String thumbnail_image_url = profile.get("thumbnail_image_url").toString();
            String email = profile.get("email").toString();
            //String email = kakao_account.get("email").toString();

            userInfo.put("nickname", nickname);
            userInfo.put("thumbnail_image_url", thumbnail_image_url);
            userInfo.put("email", email);

        } catch (IOException e) {
            e.printStackTrace();
        } catch ( ParseException e){
            e.printStackTrace();
        }

        return userInfo;
    }
}
