package com.cafe.cafeproject.common.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "t_user_info")
@ToString(callSuper = true)
public class UserInfoDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userid;

    private String email;
}
