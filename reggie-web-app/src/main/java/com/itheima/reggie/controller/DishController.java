package com.itheima.reggie.controller;

import com.itheima.reggie.DishService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DishController {
    @Autowired
    private DishService dishService;

    //查找菜单中的菜品
    @GetMapping("/dish/list")
    public ResultInfo findDish(Long categoryId,Integer status){
        List<Dish> dishList =  dishService.findDish(categoryId,status);
        return ResultInfo.success(dishList);
    }
}
