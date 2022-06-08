package com.itheima.reggie.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.AddressService;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.mapper.AddressMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.itheima.reggie.domain.Address.DEFAULT_ADDRESS;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    //查询列表
    @Override
    public List<Address> findList() {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, UserHolder.get().getId());
        return addressMapper.selectList(wrapper);
    }

    //设置默认
    @Override
    public void setDefault(Long id) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, UserHolder.get().getId());
        //设置更新字段
        Address address = new Address();
        address.setIsDefault(Address.NOT_DEFAULT_ADDRESS);
        //更新
        addressMapper.update(address, wrapper);
        //设置当前id为默认地址
        address.setIsDefault(DEFAULT_ADDRESS);
        address.setId(id);
        addressMapper.updateById(address);
    }

    //查询
    @Override
    public Address findDefault() {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, UserHolder.get().getId()).eq(Address::getIsDefault, DEFAULT_ADDRESS);
        return addressMapper.selectOne(wrapper);
    }

    //回显
    @Override
    public Address findById(Long id) {
        return addressMapper.selectById(id);
    }

    //修改
    @Override
    public void update(Address address) {
        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        //当前用户 当前地址
        wrapper.eq(Address::getUserId, UserHolder.get().getId()).eq(Address::getId, address.getId());
        addressMapper.delete(wrapper);
        addressMapper.insert(address);
    }

    //新增
    @Override
    public void add(Address address) {
        address.setUserId(UserHolder.get().getId());
        addressMapper.insert(address);
    }

    //删除
    @Override
    public void delete(Long[] ids) {
        if (ids != null && ids.length > 0) {
            addressMapper.deleteBatchIds(Arrays.asList(ids));
        }
    }
}
