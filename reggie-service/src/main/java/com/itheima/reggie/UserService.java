package com.itheima.reggie;

import com.itheima.reggie.domain.User;

import java.util.List;

//前台用户
public interface UserService {

    //根据phone查找
    User findByPhone(String phone);

    //保存
    void save(User user);

}
