package com.cafe.cafeproject.common.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "t_cafe_keyword_info")
@IdClass(CafeKeywordInfoPKDto.class)
@ToString(callSuper = true)
public class CafeKeywordInfoDto {

    @Id
    private int id;

    @Id
    private String keywordId;

    @ManyToOne
    @JoinColumn(name = "id")
    private CafeinfoDto cafeinfoDto;
}
