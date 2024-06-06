package com.cafe.cafeproject.main.controller;

import com.cafe.cafeproject.common.dto.KeywordDto;
import com.cafe.cafeproject.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
     * 키워드 전체 목록 조회 - 검색 용도
     * @return
     */
    @GetMapping("/getTotKeywordList")
    public List<KeywordDto> getTotKeywordList(){
        return null;
    }
}
