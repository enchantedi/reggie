package com.itheima.reggie;

import com.itheima.reggie.domain.Cart;

import java.util.List;

public interface CartService {
    //添加
    Cart add(Cart cart);

    //查找购物车
    List<Cart> find();

    //修改购物车
    Cart sub(Cart cart);

    //清空购物车
    void clean();
}
