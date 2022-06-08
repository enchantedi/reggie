package com.itheima.reggie.controller;

import com.itheima.reggie.AddressService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    //查询列表
    @GetMapping("/address/list")
    public ResultInfo findList() {
        List<Address> addressList = addressService.findList();
        return ResultInfo.success(addressList);
    }

    //设置默认
    @PutMapping("/address/default")
    public ResultInfo setDefault(@RequestBody Address address) {
        addressService.setDefault(address.getId());
        return ResultInfo.success(null);
    }

    //查询默认
    @GetMapping("/address/default")
    public ResultInfo findDefault() {
        Address address = addressService.findDefault();
        if (address == null) {
            return ResultInfo.error("不存在默认地址！");
        }
        return ResultInfo.success(address);
    }

    //回显
    @GetMapping("/address/{id}")
    public ResultInfo findById(@PathVariable("id") Long id) {
        Address address = addressService.findById(id);
        return ResultInfo.success(address);
    }

    //修改
    @PutMapping("/address")
    public ResultInfo update(@RequestBody Address address) {
        addressService.update(address);
        return ResultInfo.success(null);
    }

    //删除
    @DeleteMapping("/address")
    public ResultInfo delete(@RequestParam("ids")Long[] ids){
        addressService.delete(ids);
        return ResultInfo.success(null);
    }

    //新增
    @PostMapping("/address")
    public ResultInfo add(@RequestBody Address address){
        addressService.add(address);
        return ResultInfo.success(null);
    }
    
}
