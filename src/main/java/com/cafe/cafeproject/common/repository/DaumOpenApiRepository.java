package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.CafeinfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaumOpenApiRepository extends JpaRepository<CafeinfoDto, String> {

}
