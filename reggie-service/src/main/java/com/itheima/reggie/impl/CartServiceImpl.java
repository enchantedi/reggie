package com.itheima.reggie.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.CartService;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Cart;
import com.itheima.reggie.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    //添加购物车
    @Override
    public Cart add(Cart cart) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        //查询只有两种情况 传入的是dishId 传入的是setmealId
        wrapper.eq(cart.getDishId() != null, Cart::getDishId, cart.getDishId());
        wrapper.eq(cart.getSetmealId() != null, Cart::getSetmealId, cart.getSetmealId());
        //从threadLocal获取当前用户id
        wrapper.eq(Cart::getUserId, UserHolder.get().getId());

        Cart cartFromDB = cartMapper.selectOne(wrapper);
        //查询结果为空则创建
        if (cartFromDB == null) {
            //设置id
            cart.setUserId(UserHolder.get().getId());
            //设置number为1
            cart.setNumber(1);
            //设置更新时间
            cart.setCreateTime(new Date());
            //更新数据库
            cartMapper.insert(cart);
            return cart;
        } else {
            //查询结果不为空则number + 1
            cartFromDB.setNumber(cartFromDB.getNumber() + 1);
            //更新数据库
            cartMapper.updateById(cartFromDB);
            return cartFromDB;
        }
    }

    //查找购物车
    @Override
    public List<Cart> find() {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, UserHolder.get().getId());
        return cartMapper.selectList(wrapper);
    }

    //修改购物车(减少)
    @Override
    public Cart sub(Cart cart) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(cart.getDishId() != null, Cart::getDishId, cart.getDishId());
        wrapper.eq(cart.getSetmealId() != null, Cart::getSetmealId, cart.getSetmealId());
        wrapper.eq(Cart::getUserId, UserHolder.get().getId());

        Cart cartFromDB = cartMapper.selectOne(wrapper);
        Integer number = cartFromDB.getNumber();
        if (number > 1) {
            cartFromDB.setNumber(number - 1);
            cartMapper.updateById(cartFromDB);
            return cartFromDB;
        } else {
            cartMapper.delete(wrapper);
            return cart;
        }
    }

    //清空购物车
    @Override
    public void clean() {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId,UserHolder.get().getId());
        cartMapper.delete(wrapper);
    }
}
