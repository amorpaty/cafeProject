package com.cafe.cafeproject.main.service;

import com.cafe.cafeproject.common.config.QueryDSLConfig;
import com.cafe.cafeproject.common.dto.CafeinfoDto;
import com.cafe.cafeproject.common.dto.KeywordDto;
import com.cafe.cafeproject.common.repository.KeywordRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static com.cafe.cafeproject.common.dto.QCafeKeywordInfoDto.cafeKeywordInfoDto;
import static com.cafe.cafeproject.common.dto.QCafeinfoDto.cafeinfoDto;

/**
 * Main 화면 Service
 * @since 2024.06.06
 * @author 은주
 */
@Service
@RequiredArgsConstructor
public class MainService {

    @Value("${kakao.apiKey}")
    private String kakaoApiKey;

    private final KeywordRepository keywordRepository;

    private final QueryDSLConfig queryDSLConfig;

    /**
     * 키워드 목록 조회 (상위 5개)
     */
    public List<KeywordDto> getKeywordList() {
        return keywordRepository.getKeywordList();
    }

    /**
     * 카페 List 목록 조회 (POI 표출 용)
     * @return
     */
    public List<CafeinfoDto> getCafeList(Map<String, Object> params){
        List<CafeinfoDto> resultList = new ArrayList<>();
        String searchKeyword = params.get("searchKeyword").toString();
        String  [] searchKeywordArr = null;
        List<String> searchKeywordList = null;

        if (!ObjectUtils.isEmpty(params.get("searchKeywordList").toString())){
            searchKeywordArr = (String[]) params.get("searchKeywordList").toString().trim().split(",");
            searchKeywordList = Arrays.asList(searchKeywordArr);
        }
        if(StringUtils.isNotEmpty(searchKeyword) && CollectionUtils.isNotEmpty(searchKeywordList)){
            resultList = queryDSLConfig.jpaQueryFactory().query()
                    .select(cafeinfoDto)
                    .leftJoin(cafeKeywordInfoDto)
                    .where(((cafeinfoDto.place_name.contains(searchKeyword))
                            .or(cafeinfoDto.road_address_name.contains(searchKeyword))
                            .or(cafeinfoDto.address_name.contains(searchKeyword)))
                            .and(cafeKeywordInfoDto.keywordId.in(searchKeywordList))).stream().toList();
        }else if(StringUtils.isNotEmpty(searchKeyword) && CollectionUtils.isEmpty(searchKeywordList)){
            resultList = queryDSLConfig.jpaQueryFactory().query()
                    .select(cafeinfoDto)
                    .leftJoin(cafeKeywordInfoDto)
                    .where((cafeinfoDto.place_name.contains(searchKeyword))
                            .or(cafeinfoDto.road_address_name.contains(searchKeyword))
                            .or(cafeinfoDto.address_name.contains(searchKeyword))).stream().toList();
        }else if(StringUtils.isEmpty(searchKeyword) && CollectionUtils.isNotEmpty(searchKeywordList)){
            resultList = queryDSLConfig.jpaQueryFactory()
                    .select(cafeinfoDto)
                    .leftJoin(cafeKeywordInfoDto)
                    .where(cafeKeywordInfoDto.keywordId.in(searchKeywordList)).stream().toList();
        }
        return resultList;
    }

    /**
     * 카페 썸네일 검색 API
     * @param params
     * @return
     */
    public Map<String, Object> getCafeThumnail(Map<String, Object> params)  {
        Map<String, Object> result = new HashMap<>();

        try {
            String text = URLEncoder.encode(params.get("place_name").toString(),"UTF-8");
            String apiURL = "https://dapi.kakao.com/v2/search/image?query=" + text + "&page=1&size=1";
            JSONParser jsonParser = new JSONParser();
            String responseBody = this.get(apiURL);

            JSONObject jsonObject = (JSONObject)jsonParser.parse(responseBody);
            List<Map<String, String>> documents = (ArrayList)jsonObject.get("documents");

            if(CollectionUtils.isNotEmpty(documents)){
                result.put("image_url",documents.get(0).get("image_url"));
                result.put("thumbnail_url", documents.get(0).get("thumbnail_url"));
                return result;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }

        return result;
    }

    public String get(String apiUrl) {
        HttpURLConnection con = this.connect(apiUrl);

        String var4;
        try {
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK " + this.kakaoApiKey);
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                var4 = this.readBody(con.getInputStream());
                return var4;
            }

            var4 = this.readBody(con.getErrorStream());
        } catch (IOException var8) {
            throw new RuntimeException("API 요청과 응답 실패", var8);
        } finally {
            con.disconnect();
        }

        return var4;
    }

    public HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException var3) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, var3);
        } catch (IOException var4) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, var4);
        }
    }

    public String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try {
            BufferedReader lineReader = new BufferedReader(streamReader);

            String var6;
            try {
                StringBuilder responseBody = new StringBuilder();

                while(true) {
                    String line;
                    if ((line = lineReader.readLine()) == null) {
                        var6 = responseBody.toString();
                        break;
                    }

                    responseBody.append(line);
                }
            } catch (Throwable var8) {
                try {
                    lineReader.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }

                throw var8;
            }

            lineReader.close();
            return var6;
        } catch (IOException var9) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", var9);
        }
    }
}