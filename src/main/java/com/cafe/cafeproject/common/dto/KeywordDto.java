package com.cafe.cafeproject.common.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "t_keyword")
@ToString(callSuper = true)
public class KeywordDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String keywordId;

    @Column
    private String keywordName;
}
