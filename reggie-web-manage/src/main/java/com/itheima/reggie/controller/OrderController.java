package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.OrderService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    //查看订单明细
    @GetMapping("/order/page")
    public ResultInfo findByPage(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 String number, String beginTime, String endTime) {
        Page<Order> page = orderService.findByPage(pageNum, pageSize, number, beginTime, endTime);
        return ResultInfo.success(page);
    }

    //改变订单状态
    @PutMapping("/order")
    public ResultInfo update(@RequestBody Order order) {
        orderService.update(order);
        return ResultInfo.success(null);
    }
}
