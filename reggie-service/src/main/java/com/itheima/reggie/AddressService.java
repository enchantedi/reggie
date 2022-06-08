package com.itheima.reggie;

import com.itheima.reggie.domain.Address;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AddressService {
    //查询列表
    List<Address> findList();

    //设置默认
    void setDefault(Long id);

    //查询默认
    Address findDefault();

    //回显地
    Address findById(Long id);

    //修改
    void update(Address address);

    //新增
    void add(Address address);

    //删除
    void delete(Long[] ids);
}
