package com.cafe.cafeproject.common.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class CafeKeywordInfoPKDto implements Serializable {
    private String id;
    private String keywordId;
}
