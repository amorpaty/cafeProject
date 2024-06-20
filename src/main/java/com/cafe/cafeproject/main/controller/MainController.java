package com.cafe.cafeproject.main.controller;

import com.cafe.cafeproject.common.dto.CafeinfoDto;
import com.cafe.cafeproject.common.dto.KeywordDto;
import com.cafe.cafeproject.main.service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(("/main"))
@RequiredArgsConstructor
public class MainController {

    @Value("${kakao.apiKey}")
    private String kakaoApiKey;

    @Value("${kakao.redirectUri}")
    private String redirectUri;

    private final MainService mainService;

    @GetMapping
    public ModelAndView mainView()
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("apiKey", kakaoApiKey);
        mv.addObject("redirectUri", redirectUri);
        mv.setViewName("index.html");
        return mv;
    }

    /**
     * 키워드 목록 조회 (상위 5개) - 검색 용도
     * @return
     */
    @GetMapping("/getKeywordList")
    public List<KeywordDto> getKeywordList(){
        return mainService.getKeywordList();
    }

    /**
     * 카페 목록 조회 (조건 검색)
     * @param params
     * @return
     */
    @PostMapping("/getCafeList")
    public List<CafeinfoDto> getCafeinfoDtoList(@RequestParam Map<String, Object> params){
        return mainService.getCafeList(params);
    }

    /**
     * 카페 썸네일 조회 (카카오 API 사용)
     * @param param
     * @return
     */
    @PostMapping("/getCafeThumnail")
    public Map<String, Object> getCafeThumnail(@RequestParam Map<String, Object> param){
        return mainService.getCafeThumnail(param);
    }
}
