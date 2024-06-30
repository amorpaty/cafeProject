package com.cafe.cafeproject.common.repository;

import com.cafe.cafeproject.common.dto.CafeinfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaumOpenApiRepository extends JpaRepository<CafeinfoDto, String> {
    
    @Query(value="select " +
            " *" +
            "from " +
            "(" +
            "select" +
            "    ROW_NUMBER() OVER() AS ROWNUM," +
            "    A.*" +
            " from    " +
            "    (select" +
            "    distinct A.*" +
            "    from" +
            "        (select" +
            "            distinct  BE.*  " +
            "        from" +
            "            \"cafeProject\".T_cafeinfo AS BE ) as A  " +
            "    inner join" +
            "        (select" +
            "            distinct id " +
            "        from" +
            "            \"cafeProject\".t_cafe_keyword_info " +
            "        group by" +
            "            id) as B  " +
            "            ON A.ID != B.ID" +
            "     )  A" +
            ") RESLT     " +
            "where ROWNUM > 908 ", nativeQuery = true)
    public List<CafeinfoDto> findCafeInfoList();
}
