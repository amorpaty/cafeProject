package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.SidoinfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
시도 Repository
 */
@Repository
public interface SidoCodeRepository extends JpaRepository<SidoinfoDto, String> {

}
