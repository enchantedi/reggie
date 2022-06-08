package com.itheima.reggie.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.UserService;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.User;
import com.itheima.reggie.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//用户
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    //根据phone查找
    @Override
    public User findByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,phone);
        return userMapper.selectOne(wrapper);
    }

    //保存
    @Override
    public void save(User user) {
        userMapper.insert(user);
    }

}
