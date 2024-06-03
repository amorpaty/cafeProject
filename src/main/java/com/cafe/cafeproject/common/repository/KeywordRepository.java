package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.KeywordDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 키워드 목록 Repository
 */
@Repository
public interface KeywordRepository extends JpaRepository<KeywordDto, String> {

}
