package com.cafe.cafeproject.common.dto;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class CafeKeywordInfoPKDto implements Serializable {
    private String id;
    private String keywordId;
}
