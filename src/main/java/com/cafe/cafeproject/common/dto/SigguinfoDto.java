package com.cafe.cafeproject.common.dto;

import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.Data;

@Data
@Entity
@Table(name = "t_sigginfo")
public class SigguinfoDto {

    @Id
    @Column
    @Description("시군구코드")
    private String siggCode;


    @ManyToOne(optional = false)
    @JoinColumn(name = "sidoCode", referencedColumnName = "sidoCode")
    private SidoinfoDto sidoinfoDto;

    @Column
    @Description("시군구명")
    private String siggName;

}
