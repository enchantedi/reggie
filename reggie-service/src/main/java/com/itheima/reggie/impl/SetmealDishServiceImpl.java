package com.itheima.reggie.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.SetmealDishService;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.SetmealDish;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.mapper.SetmealDishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SetmealDishServiceImpl implements SetmealDishService {
    //查看套餐中的菜品
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
    @Override
    public List<SetmealDish> find(Long setmealId) {
        LambdaUpdateWrapper<SetmealDish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealId);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(wrapper);

        ArrayList<SetmealDish> setmealDishList = new ArrayList<>();
        for (SetmealDish setmealDish : setmealDishes) {
                Dish dish = dishMapper.selectById(setmealDish.getDishId());
                setmealDish.setDishId(dish.getId());
                setmealDish.setImage(dish.getImage());
                setmealDish.setDescription(dish.getDescription());
                setmealDishList.add(setmealDish);
        }
        return setmealDishList;
    }
}
