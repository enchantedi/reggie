package com.itheima.reggie;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;

import java.util.List;

public interface DishService {
    //分页查询
    Page<Dish> findByPage(String name, Integer pageNum, Integer pageSize);

    //保存
    void save(Dish dish);

    //修改
    void update(Dish dish);

    //回显
    Dish findById(Long id);


    //删除停售状态
    ResultInfo delete(Integer[] ids);

    //停售/启售
    void openAndClose(Integer status, Long[] ids);

    //删除停售状态分类列表
    void delete(Long[] ids);

    //查看分类查看菜品
    //List<Dish> findByCategoryId(Long categoryId);

    //查找菜单中的菜品和套餐
    List<Dish> findDish(Long categoryId, Integer status);

    //查看菜品分类&名称
    List<Dish> findDish(Long categoryId,String name);
}
