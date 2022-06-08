package com.itheima.reggie.domain;

import lombok.Data;

import java.io.Serializable;

//用户信息
@Data
public class User implements Serializable {
    //状态标识常量
    public static final Integer STATUS_DISABLE = 0;
    public static final Integer STATUS_ENABLE = 1;

    private Long id;//主键

    private String name;//姓名

    private String phone;//手机号

    private String sex; //性别 0 女 1 男

    private String idNumber;//身份证号

    private String avatar;//头像

    private Integer status;//状态 0:禁用，1:正常
}