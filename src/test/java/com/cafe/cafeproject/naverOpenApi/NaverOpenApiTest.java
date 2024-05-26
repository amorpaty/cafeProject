package com.cafe.cafeproject.naverOpenApi;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class NaverOpenApiTest {

    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.clientSecret}")
    private String clientSecret;

    private static int sunbun = 1;
    private static int displaySun = 5;

    private static boolean flag = true;

    /**
     * 네이버 지도 지역 검색 API
     * numeunju
     * 
     * -- 키워드에 대한 데이터를 3개씩 밖에 안가져와서
     * -- 내가 진행하려는 프로젝트와는 맞지 않는다.
     * @since 2024.05.09
     *
     */
    @Test
    public void naverOpenApiTest(){

        String text = null;
        String [] regionArr = {"서울카페"};

        try {
            text = URLEncoder.encode(regionArr[0], "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }

        JSONParser jsonParser = new JSONParser();

        while(flag){
            try {
                Thread.sleep(1000);

                String apiURL = "https://openapi.naver.com/v1/search/local.xml?query=" + text + "&display=" + displaySun + "&start=" + sunbun + "&sort=comment";    // JSON 결과

                System.out.println("apiURL : " + apiURL);

                Map<String, String> requestHeaders = new HashMap<>();
                requestHeaders.put("X-Naver-Client-Id", clientId);
                requestHeaders.put("X-Naver-Client-Secret", clientSecret);
                String responseBody = get(apiURL,requestHeaders);

                JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);

                System.out.println(jsonObject.toString());

                int total = Integer.parseInt(jsonObject.get("total").toString());
                int display = Integer.parseInt(jsonObject.get("display").toString());

                if(total == 0){
                    flag = false;
                }else{
                    //값이 있으면 ~~
                    List<Map<String, String>> items =  (ArrayList<Map<String, String>>) jsonObject.get("items");

                    if(!CollectionUtils.isEmpty(items)){
                        items.forEach(item -> {
                            System.out.println(item);
                        });

                        sunbun += display;
                        displaySun += display;
                    }
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }

    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                flag = false;
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;

            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}