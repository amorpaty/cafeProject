package com.cafe.cafeproject.common.dto;

import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.Data;
import org.locationtech.jts.geom.Polygon;


@Data
@Entity
@Table(name = "t_sidoinfo")
public class SidoinfoDto {

    @Id
    @Column
    @Description("시도코드")
    private String sidoCode;

    @Column
    @Description("시도명")
    private String sidoName;

    @Column
    @Description("geometry")
    private String geom;
}
