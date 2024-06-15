package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.CafeinfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeInfoRepository extends JpaRepository<CafeinfoDto, String>, QuerydslPredicateExecutor<CafeinfoDto> {

}
