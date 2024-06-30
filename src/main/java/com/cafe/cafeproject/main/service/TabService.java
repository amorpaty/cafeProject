package com.cafe.cafeproject.main.service;

import com.cafe.cafeproject.common.config.QueryDSLConfig;
import com.cafe.cafeproject.common.dto.CafeMenuInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cafe.cafeproject.common.dto.QCafeMenuInfoDto.cafeMenuInfoDto;
import static com.cafe.cafeproject.common.dto.QCafeinfoDto.cafeinfoDto;


/**
 * Cafe Tab menu Service
 * @since 2024.06.29
 * @author 은주
 */
@Service
@RequiredArgsConstructor
public class TabService {

    private final MainService mainService;

    private final QueryDSLConfig queryDSLConfig;

    /**
     * 카페 메뉴 목록 조회
     * @param params
     * @return
     */
    public List<CafeMenuInfoDto> getTabCafeMenuList(Map<String, Object> params){
        return queryDSLConfig.jpaQueryFactory().query()
                .select(cafeMenuInfoDto)
                .from(cafeMenuInfoDto)
                .innerJoin(cafeinfoDto)
                .on(cafeMenuInfoDto.id.eq(cafeinfoDto.id))
                .where(cafeinfoDto.id.eq(Integer.parseInt(params.get("id").toString())))
                .fetch().stream().toList();
    }

    /**
     * 카페 사진 목록 조회
     * @param params
     * @param request
     * @return
     */
    public List<Map<String, Object>> getCafePictureList(Map<String, Object> params, HttpServletRequest request) {

        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            String text = URLEncoder.encode(params.get("place_name").toString(),"UTF-8");
            String apiURL = "https://dapi.kakao.com/v2/search/image?query=" + text;
            JSONParser jsonParser = new JSONParser();
            String responseBody = mainService.get(apiURL);

            JSONObject jsonObject = (JSONObject)jsonParser.parse(responseBody);
            List<Map<String, Object>> documents = (ArrayList)jsonObject.get("documents");

            if(CollectionUtils.isNotEmpty(documents)){
                resultList = documents;
                return resultList;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }

        return resultList;
    }
}