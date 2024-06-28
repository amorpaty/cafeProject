package com.cafe.cafeproject.common.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ValueGenerationType;

@Data
@Entity
@Table(name = "t_cafe_menuinfo")
@ToString(callSuper = true)
public class CafeMenuInfoDto {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long menuId;

    @JoinColumn
    private int id;

    @Column
    private String tit;
    @Column
    private String ditail;
    @Column
    private String price;
    @Column
    private String img;
}
