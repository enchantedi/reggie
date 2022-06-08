package com.itheima.reggie;

import com.itheima.reggie.domain.SetmealDish;

import java.util.List;

public interface SetmealDishService {

    //查看套餐中的菜品
    List<SetmealDish> find(Long setmealId);
}
