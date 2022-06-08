package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.DishService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    //分页查询
    @GetMapping("/dish/page")
    public ResultInfo findByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "7") Integer pageSize, String name) {
        Page<Dish> page = dishService.findByPage(name, pageNum, pageSize);
        return ResultInfo.success(page);
    }

    //保存
    @PostMapping("/dish")
    public ResultInfo save(@RequestBody Dish dish) {
        dishService.save(dish);
        return ResultInfo.success(null);
    }

    //回显
    @GetMapping("/dish/{id}")
    public ResultInfo findById(@PathVariable("id") Long id) {
        Dish dish = dishService.findById(id);
        return ResultInfo.success(dish);
    }

    //修改
    @PutMapping("/dish")
    public ResultInfo update(@RequestBody Dish dish) {
        dishService.update(dish);
        return ResultInfo.success(null);
    }

    //停售/启售
    @PostMapping("/dish/status/{status}")
    public ResultInfo openAndClose(@PathVariable("status") Integer status, Long[] ids) {
        dishService.openAndClose(status, ids);
        return ResultInfo.success(null);
    }

    //删除停售状态
    @DeleteMapping("/dish")
    public ResultInfo delete(Long[] ids) {
        dishService.delete(ids);
        return ResultInfo.success(null);
    }


    //查看菜品分类
    @GetMapping("/dish/list")
    public ResultInfo findDish(Long categoryId,String name){
       List<Dish> categoryList =  dishService.findDish(categoryId,name);
       return ResultInfo.success(categoryList);
    }
}