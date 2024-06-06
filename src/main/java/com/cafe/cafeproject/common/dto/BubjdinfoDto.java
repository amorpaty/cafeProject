package com.cafe.cafeproject.common.dto;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jdk.jfr.Description;
import lombok.Data;


@Data
@Entity
@Table(name = "t_bubjdinfo")
public class BubjdinfoDto {

    @Id
    @Column
    @Description("법정동코드")
    private String bubjdCode;

    @Column
    @Description("시도코드")
    private String sidoCode;

    @Column
    @Description("시군구코드")
    private String siggCode;

    @Column
    @Description("법정동명")
    private String bubjdName;
}
