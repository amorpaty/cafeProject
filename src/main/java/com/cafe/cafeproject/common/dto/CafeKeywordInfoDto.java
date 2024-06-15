package com.cafe.cafeproject.common.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "t_cafe_keyword_info")
@IdClass(CafeKeywordInfoPKDto.class)
@ToString(callSuper = true)
public class CafeKeywordInfoDto {

    @Id
    private String id;

    @Id
    private String keywordId;

    @ManyToOne
    @JoinColumn(name = "id")
    private CafeinfoDto cafeinfoDto;
}
