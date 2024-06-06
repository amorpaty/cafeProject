package com.cafe.cafeproject.main.service;

import com.cafe.cafeproject.common.dto.KeywordDto;
import com.cafe.cafeproject.common.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Main 화면 Service
 * @since 2024.06.06
 * @author 은주
 */
@Service
@RequiredArgsConstructor
public class MainService {

    private final KeywordRepository keywordRepository;

    /**
     * 키워드 목록 조회 (상위 5개)
     */
    public List<KeywordDto> getKeywordList() {
        return keywordRepository.getKeywordList();
    }

    /**
     * 키워드 목록 전체 조회
     */
    public List<KeywordDto> getTotKeywordList() {
        return null;
    }
}