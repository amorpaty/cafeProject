package com.cafe.cafeproject.daumOpenApi;

import com.cafe.cafeproject.common.dto.CafeinfoDto;
import com.cafe.cafeproject.repository.DaumOpenApiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DaumOpenApiTest {

    @Value("${kakao.apiKey}")
    private String kakaoApiKey;

    @Autowired
    DaumOpenApiRepository daumOpenApiRepository;

    /**
     * daum 지도 지역 검색 API
     * numeunju
     * @since 2024.05.09
     *
     */
    @Test
    public void daumOpenApiTest(){

        String text = null;
        String [] regionArr = {"서초 카페", "강남 카페", "논현 카페", "신사 카페",
                "양재 카페", "매봉 카페", "안국 카페", "종로 카페", "동대문 카페", "혜화 카페", "인사동 카페", "서울 카페"};

        int page = 1;
        boolean flag = true;

        JSONParser jsonParser = new JSONParser();
        String isEnd = "true";

        for (int i = 0; i < regionArr.length ; i++) {

            while(flag){
                try {
                    text = URLEncoder.encode(regionArr[i], "UTF-8");

                    System.out.println("regionArr : "  + i + "page : " + page);

                    String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + text + "&category_group_code=CE7&page=" + page;    // JSON 결과

                    System.out.println("apiURL : " + apiURL);

                    String responseBody = get(apiURL);

                    JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
                    ObjectMapper mapper = new ObjectMapper();

                    System.out.println(jsonObject.toString());
                    isEnd = ((JSONObject) jsonObject.get("meta")).get("is_end").toString();

                    List<Map<String, String>> documents = (ArrayList<Map<String,String>>) jsonObject.get("documents");

                    //값이 있으면 DB 적재
                    if (!CollectionUtils.isEmpty(documents)) {

                        List<CafeinfoDto> cafeinfoDtos = new ArrayList<>();
                        //DB 적재
                        for (Map<String, String> map : documents) {
                            CafeinfoDto cafeinfoDto = mapper.convertValue(map, CafeinfoDto.class);
                            cafeinfoDtos.add(cafeinfoDto);
                        }
                        daumOpenApiRepository.saveAll(cafeinfoDtos);
                    }
                    // is-end : 현재 페이지가 마지막 페이지인지 여부
                    // 값이 false면 다음 요청 시 page 값을 증가시켜 다음 페이지 요청 가능
                    if (isEnd.equalsIgnoreCase("true")) {
                        page = 1;
                        flag = false;
                        continue;
                    } else {
                        page++;
                    }
                    //REST API
                    Thread.sleep(1000);

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("검색어 인코딩 실패",e);
                }
            }

            if(i != regionArr.length-1 ){
                flag = true;
            }
        }
    }

    private String get(String apiUrl){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");

           // for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty("Authorization", "KakaoAK " + kakaoApiKey);
           // }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private String readBody(InputStream body){
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
