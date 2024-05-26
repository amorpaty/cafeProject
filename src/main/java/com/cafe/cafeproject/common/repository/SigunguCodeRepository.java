package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.SigguinfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 시군구 Repository
 */
@Repository
public interface SigunguCodeRepository extends JpaRepository<SigguinfoDto, String> {

}
