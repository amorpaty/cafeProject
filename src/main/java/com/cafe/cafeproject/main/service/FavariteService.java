package com.cafe.cafeproject.main.service;

import com.cafe.cafeproject.common.config.QueryDSLConfig;
import com.cafe.cafeproject.common.dto.CafeinfoDto;
import com.cafe.cafeproject.common.dto.FavarityCafeDto;
import com.cafe.cafeproject.common.dto.UserInfoDto;
import com.cafe.cafeproject.common.repository.UserInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.cafe.cafeproject.common.dto.QFavarityCafeDto.favarityCafeDto;
import static com.cafe.cafeproject.common.dto.QCafeinfoDto.cafeinfoDto;
import static com.cafe.cafeproject.common.dto.QUserInfoDto.userInfoDto;

/**
 * Favarite Cafe Service
 * @since 2024.06.22
 * @author 은주
 */
@Service
@RequiredArgsConstructor
public class FavariteService {

    private final QueryDSLConfig queryDSLConfig;

    private final UserInfoRepository userInfoRepository;

    /**
     * 카페 찜 여부 조회
     * @param param
     * @param request
     * @return
     */
    public Object findFavariteCafe(Map<String, Object> param, HttpServletRequest request){

        Map<String, Object> userInfo = (Map<String, Object>) request.getSession().getAttribute("userInfo");

        Object obj = null;
        if(!CollectionUtils.isEmpty(userInfo)){

            Optional<UserInfoDto> userDto = queryDSLConfig.jpaQueryFactory().query()
                    .select(userInfoDto).from(userInfoDto).where(userInfoDto.email.eq(userInfo.get("email").toString())).fetch().stream().findAny();

            obj = queryDSLConfig.jpaQueryFactory().query()
                    .select(favarityCafeDto.favId)
                    .from(favarityCafeDto)
                    .where(favarityCafeDto.id.eq(Integer.parseInt(param.get("id").toString()))
                            .and(favarityCafeDto.userId.eq(userDto.get().getUserid()))).fetch();
        }
        return obj;
    }

    /**
     * 카페 찜 목록 조회 (사용자별)
     * @param favarityDto
     * @return
     */
    public List<CafeinfoDto> findFavariteCafeList(FavarityCafeDto favarityDto){
        return queryDSLConfig.jpaQueryFactory().query()
                .select(cafeinfoDto)
                .from(cafeinfoDto)
                .innerJoin(favarityCafeDto)
                .on(cafeinfoDto.id.eq(favarityCafeDto.id))
                .where(favarityCafeDto.userId.eq(favarityDto.getUserId())).fetch().stream().toList();
    }
}