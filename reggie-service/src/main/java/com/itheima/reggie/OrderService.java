package com.itheima.reggie;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Order;

public interface OrderService {
    //保存
    void save(Order order);

    //查看(客户)
    Page<Order> find(Integer pageNum, Integer pageSize);

    //查看明细(后台)
    Page<Order> findByPage(Integer pageNum, Integer pageSize, String number,String beginTime, String endTime);

    //改变订单状态
    void update(Order order);
}
