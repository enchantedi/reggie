package com.itheima.reggie.controller;

import com.itheima.reggie.CartService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Cart;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    //添加购物车
    @PostMapping("/cart/add")
    public ResultInfo add(@RequestBody Cart cart ){
        Cart cartResult = cartService.add(cart);
        return ResultInfo.success(cartResult);
    }

    //查找购物车
    @GetMapping("/cart/list")
    public ResultInfo find(){
        List<Cart> cartList = cartService.find();
        return ResultInfo.success(cartList);
    }

    //修改购物车
    @PostMapping("/cart/sub")
    public ResultInfo sub(@RequestBody Cart cart){
        Cart cartResult = cartService.sub(cart);
        return ResultInfo.success(cartResult);
    }

    //清空购物车
    @DeleteMapping("/cart/clean")
    public ResultInfo clean(){
        cartService.clean();
        return ResultInfo.success(null);
    }
}
