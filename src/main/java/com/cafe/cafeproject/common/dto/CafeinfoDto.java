package com.cafe.cafeproject.common.dto;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

@Data
@Entity
@Transactional
@Table(name = "t_cafeinfo")
public class CafeinfoDto {

    @Id
    private String id;

    @Column
    private String place_url;
    @Column
    private String place_name;
    @Column
    private String category_name;
    @Column
    private String address_name;
    @Column
    private String road_address_name;
    @Column
    private String x;
    @Column
    private String y;

    private String category_group_name;
    private String category_group_code;
    private String distance;
    private String phone;
}