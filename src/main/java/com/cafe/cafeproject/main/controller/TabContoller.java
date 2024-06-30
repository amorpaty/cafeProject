package com.cafe.cafeproject.main.controller;

import com.cafe.cafeproject.common.dto.CafeMenuInfoDto;
import com.cafe.cafeproject.main.service.TabService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 카페 상세영역 Tab Contoller
 */
@RestController
@RequestMapping("/main/tab")
@RequiredArgsConstructor
public class TabContoller {

    private final TabService tabService;

    @PostMapping("/getTabCafeMenuList")
    public List<CafeMenuInfoDto> getTabCafeMenuList(@RequestParam Map<String, Object> params){
        return tabService.getTabCafeMenuList(params);
    }


    /**
     * 사진 목록 조회
     * @param param
     * @return
     */
    @PostMapping("/getCafePictureList")
    public List<Map<String, Object>> getCafePictureList(@RequestParam Map<String, Object> params, HttpServletRequest request){
        return tabService.getCafePictureList(params, request);
    }
}
