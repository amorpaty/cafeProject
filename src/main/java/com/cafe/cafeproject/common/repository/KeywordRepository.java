package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.KeywordDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 키워드 목록 Repository
 */
@Repository
public interface KeywordRepository extends JpaRepository<KeywordDto, String> {

    @Query(value = "select COUNT(A.keyword_id) as cnt, A.keyword_id , A.keyword_name"
            + " from \"cafeProject\".t_keyword AS A"
            + " inner join \"cafeProject\".t_cafe_keyword_info AS B"
            + " on A.keyword_id = B.keyword_id"
            + " group by A.keyword_id, A.keyword_name"
            + " order by CNT DESC"
            + " limit 50", nativeQuery = true)
    public List<KeywordDto> getKeywordList();

}

