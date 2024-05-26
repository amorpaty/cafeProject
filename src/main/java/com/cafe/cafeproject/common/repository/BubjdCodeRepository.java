package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.BubjdinfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 법정동 Repository
 */
@Repository
public interface BubjdCodeRepository extends JpaRepository<BubjdinfoDto, String> {

}
