package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.CafeKeywordInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 카페 키워드 정보 Repository
 */
@Repository
public interface CafeKeywordInfoRepository extends JpaRepository<CafeKeywordInfoDto, String> {

}
