package com.cafe.cafeproject.main.controller;
import com.cafe.cafeproject.common.config.QueryDSLConfig;
import com.cafe.cafeproject.common.dto.CafeinfoDto;
import com.cafe.cafeproject.common.dto.FavarityCafeDto;
import com.cafe.cafeproject.common.dto.UserInfoDto;
import com.cafe.cafeproject.common.repository.FavariteCafeRepository;
import com.cafe.cafeproject.common.repository.UserInfoRepository;
import com.cafe.cafeproject.main.service.FavariteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.cafe.cafeproject.common.dto.QUserInfoDto.userInfoDto;

/**
 * 카페 찜목록 Contoller
 */
@RestController
@RequestMapping("/main/fav")
@RequiredArgsConstructor
public class FavariteContoller {

    private final FavariteService favariteService;

    private final FavariteCafeRepository favariteCafeRepository;

    private final QueryDSLConfig queryDSLConfig;

    private final UserInfoRepository userInfoRepository;

    /**
     * 카페 찜 여부 가져오기
     * @param param
     * @return
     */
    @PostMapping("/selectFav")
    public Map<String, Object> findCafeFavarite(@RequestParam Map<String, Object> param, HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        result.put("fav", favariteService.findFavariteCafe(param, request));
        return result;
    }

    /**
     * 카페 찜 목록 가져오기
     * @param request
     * @return
     */
    @PostMapping("/selectFavList")
    public List<CafeinfoDto> findCafeFavariteList(HttpServletRequest request){
        FavarityCafeDto favarityCafeDto = new FavarityCafeDto();
        Object obj = request.getSession().getAttribute("userInfo");
        Optional<UserInfoDto> userDto = queryDSLConfig.jpaQueryFactory().query()
                .select(userInfoDto).from(userInfoDto).where(userInfoDto.email.eq(((Map<String, Object>)obj).get("email").toString())).fetch().stream().findAny();

        favarityCafeDto.setUserId(userDto.get().getUserid());

        return favariteService.findFavariteCafeList(favarityCafeDto);
    }

    /**
     * 카페 찜 저장/해제
     * @param param
     * @param request
     * @return
     */
    @PostMapping("/saveFav")
    public Map<String, Object> saveCafeFavarite(@RequestParam Map<String, Object> param, HttpServletRequest request){

        int result = 0;
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<>();

        if(ObjectUtils.isEmpty(session.getAttribute("userInfo"))){
            return null;
        }

        if(!ObjectUtils.isEmpty(param.get("favId"))){
            param.put("favId", param.get("favId").toString());
        }

        param.put("id", Integer.parseInt(param.get("id").toString()));
        Object obj = session.getAttribute("userInfo");

        Optional<UserInfoDto> userDto = queryDSLConfig.jpaQueryFactory().query()
            .select(userInfoDto).from(userInfoDto).where(userInfoDto.email.eq(((Map<String, Object>)obj).get("email").toString())).fetch().stream().findAny();

        param.put("userid", userDto.get().getUserid());

        ModelMapper modelMapper = new ModelMapper();

        FavarityCafeDto favarityCafeDto = modelMapper.map(param, FavarityCafeDto.class);

        if(param.get("favarite").equals("Y")){
            favariteCafeRepository.save(favarityCafeDto);
        }else{
            favariteCafeRepository.delete(favarityCafeDto);
        }

        resultMap.put("fav", favariteService.findFavariteCafe(param, request));
        return resultMap;
    }
}
