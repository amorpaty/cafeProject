package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.UserInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 회원 정보 Repository
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoDto, String> {

}
