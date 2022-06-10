package com.itheima.reggie.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.CartService;
import com.itheima.reggie.OrderService;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.*;
import com.itheima.reggie.mapper.AddressMapper;
import com.itheima.reggie.mapper.OrderDetailMapper;
import com.itheima.reggie.mapper.OrderMapper;
import com.sun.org.apache.bcel.internal.generic.LADD;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.itheima.reggie.domain.Address.DEFAULT_ADDRESS;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderMapper orderMapper;


    //保存订单
    @Override
    public void save(Order order) throws CustomException {
        User user = UserHolder.get();
        //查询购物车
        List<Cart> cartList = cartService.find();
        if (CollectionUtil.isEmpty(cartList)) {
            throw new CustomException("购物车为空,无法生成订单");
        }
        //查询地址
//        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Address::getUserId,user.getId()).eq(Address::getIsDefault,DEFAULT_ADDRESS);
//        Address address = addressMapper.selectOne(wrapper);
        Address address = addressMapper.selectById(order.getAddressId());
        if (address == null) {
            throw new CustomException("收货地址不存在,无法生成订单");
        }
        //生成一个订单id
        long orderId = IdWorker.getId();

        //统计总金额
        BigDecimal total = new BigDecimal(0);

        //生成订单详情
        for (Cart cart : cartList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(cart.getName());
            //所属订单的id
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(cart.getDishId());
            orderDetail.setSetmealId(cart.getSetmealId());
            orderDetail.setDishFlavor(cart.getDishFlavor());
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setAmount(cart.getAmount());
            orderDetail.setImage(cart.getImage());

            //保存
            orderDetailMapper.insert(orderDetail);

            //金额
            total = total.add(cart.getAmount().multiply(new BigDecimal(cart.getNumber())));
        }
        //生成订单
        order.setId(orderId);
        //订单号，无额外要求，使用订单的id
        order.setNumber(orderId + "");
        order.setStatus(0);//待付款
        order.setUserId(user.getId());
        order.setOrderTime(new Date());
        order.setCheckoutTime(new Date());
        order.setAmount(total);
        order.setUserName(user.getName());
        order.setPhone(address.getPhone());
        order.setAddress(address.getDetail());
        order.setConsignee(address.getConsignee());
        orderMapper.insert(order);

        //清空购物车
        cartService.clean();
    }

    //查看订单（客户）
    @Override
    public Page<Order> find(Integer pageNum, Integer pageSize) {
        ArrayList<Order> orders = new ArrayList<>();
        //条件 要倒叙排序
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, UserHolder.get().getId()).orderByDesc();
        //结果
        Page page = orderMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        //每一个订单
        List<Order> orderList = page.getRecords();
        //遍历
        if (CollectionUtil.isNotEmpty(orderList)) {
            for (Order order : orderList) {
                //写条件
                order.setOrderTime(order.getOrderTime());
                order.setStatus(order.getStatus());
                LambdaUpdateWrapper<OrderDetail> wrapper1 = new LambdaUpdateWrapper<>();
                wrapper1.eq(OrderDetail::getOrderId, order.getId());
                List<OrderDetail> orderDetailList = orderDetailMapper.selectList(wrapper1);
                order.setOrderDetails(orderDetailList);
            }
        }
        return page;
    }

    //查看订单明细
    @Override
    public Page<Order> findByPage(Integer pageNum, Integer pageSize, String number, String beginTime, String endTime) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(number)) {
            wrapper.eq(StringUtils.isNotEmpty(number), Order::getNumber, number).between(Order::getOrderTime, beginTime, endTime);
            return orderMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        } else {
            return orderMapper.selectPage(new Page<>(pageNum, pageSize), null);
        }
    }

    //改变订单状态
    @Override
    public void update(Order order) {
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getId, order.getId());
        if (order.getStatus() == 2 || order.getStatus() == 3) {
            wrapper.set(Order::getStatus, order.getStatus() + 1);
            orderMapper.update(null, wrapper);
        }
    }
}
