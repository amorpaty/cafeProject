package com.cafe.cafeproject.common.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "t_favarity_info")
@ToString(callSuper = true)
public class FavarityCafeDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int favId;

    @JoinColumn(name = "userId")
    private int userId;

    @JoinColumn(name = "id")
    private int id;
}
