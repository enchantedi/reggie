package com.itheima.reggie.controller;

import com.itheima.reggie.SetmealDishService;
import com.itheima.reggie.SetmealService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.domain.SetmealDish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    //查找菜单中的套餐
    @GetMapping("/setmeal/list")
    public ResultInfo findSetmeal(Long categoryId, Integer status){
        List<Setmeal> setmealList =  setmealService.findSetmeal(categoryId,status);
        return ResultInfo.success(setmealList);
    }

    //查看套餐中的菜品
    @GetMapping("/setmeal/dish/{setmealId}")
    public ResultInfo find(@PathVariable Long setmealId){
        List<SetmealDish> setmealDishList = setmealDishService.find(setmealId);
        return ResultInfo.success(setmealDishList);
    }
}
