package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.OrderService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    //保存
    @PostMapping("/order/submit")
    public ResultInfo save(@RequestBody Order order){
        orderService.save(order);
        return ResultInfo.success(null);
    }

    //查看
    @GetMapping("/order/userPage")
    public ResultInfo find(@RequestParam("page") Integer pageNum,
                           @RequestParam Integer pageSize){
        Page<Order> page = orderService.find(pageNum,pageSize);
        return ResultInfo.success(page);
    }
}
