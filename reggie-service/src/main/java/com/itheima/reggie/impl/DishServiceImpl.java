package com.itheima.reggie.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.DishService;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.DishFlavor;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.mapper.DishMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.itheima.reggie.domain.Dish.STATUS_DISABLE_SOLD;
import static com.itheima.reggie.domain.Dish.STATUS_ENABLE_SOLD;


@Transactional
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    //分页查询
    @Override
    public Page<Dish> findByPage(String name, Integer pageNum, Integer pageSize) {
        //设置从dish表中分页查询的条件
        LambdaQueryWrapper<Dish> categoryWrapper = new LambdaQueryWrapper<>();
        //StringUtils 判断某个值是否为非空
        // org.apache.commons.lang3.StringUtils;
        categoryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        //分页查询
        Page<Dish> page = dishMapper.selectPage(new Page<>(pageNum, pageSize), categoryWrapper);
        //获取页面信息的的集合并遍历
        List<Dish> dishList = page.getRecords();
        if (CollectionUtil.isNotEmpty(dishList)) {
            for (Dish dish : dishList) {
                //根据dish查到的CategoryId查找Category表中对应的名字
                Category category = categoryMapper.selectById(dish.getCategoryId());
                dish.setCategoryName(category.getName());

                //根据菜品id从DishFlavor表查询当前id菜品的口味列表
                LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();
                dishFlavorWrapper.eq(DishFlavor::getDishId, dish.getId());
                List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(dishFlavorWrapper);
                dish.setFlavors(dishFlavorList);
            }
        }
        //返回页面信息
        return page;
    }

    //保存
    @Override
    public void save(Dish dish) {
        //保存到dish 会自动返回id
        dishMapper.insert(dish);
        //实体类中有flavors，但是数据表中不存在,仅仅用于页面显示
        List<DishFlavor> flavorList = dish.getFlavors();
        if (CollectionUtil.isNotEmpty(flavorList)) {
            for (DishFlavor dishFlavor : flavorList) {
                //给口味设置相应的菜品id
                dishFlavor.setDishId(dish.getId());
                //口味信息保存到dish_flavor
                dishFlavorMapper.insert(dishFlavor);
            }
        }
    }

    //回显
    @Override
    public Dish findById(Long id) {
        //dish dish信息
        Dish dish = dishMapper.selectById(id);
        //dish_flavor 口味
        LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();
        dishFlavorWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(dishFlavorWrapper);
        if (CollectionUtil.isNotEmpty(dishFlavors)) {
            for (DishFlavor dishFlavor : dishFlavors) {
                dish.setFlavors(dishFlavors);
            }
        }
        //category 分类名
        Category category = categoryMapper.selectById(dish.getCategoryId());
        dish.setCategoryName(category.getName());

        return dish;
    }


    @Override
    public ResultInfo delete(Integer[] ids) {
        return null;
    }


    //修改
    @Override
    public void update(Dish dish) {
        //保存到dish
        dishMapper.updateById(dish);
        //删除口味表中信息
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());
        dishFlavorMapper.delete(wrapper);
        //添加新的口味信息
        List<DishFlavor> dishFlavors = dish.getFlavors();
        if (CollectionUtil.isNotEmpty(dishFlavors)) {
            for (DishFlavor dishFlavor : dishFlavors) {
                dishFlavor.setDishId(dish.getId());
                dishFlavorMapper.insert(dishFlavor);
            }
        }
    }

    //停售/启售
    @Override
    public void openAndClose(Integer status, Long[] ids) {
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                wrapper.eq(Dish::getId, id).set(Dish::getStatus, status);
                dishMapper.update(null, wrapper);
            }
        }
    }

    //删除停售状态
    @Override
    public void delete(Long[] ids) {
//        1）只允许删除停售状态的
//        2) 删除菜品的同时需要删除菜品的口味信息
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        //id在ids中 status为1 不允许删除
        wrapper.in(Dish::getId, ids).eq(Dish::getStatus, STATUS_ENABLE_SOLD);
        if ((dishMapper.selectCount(wrapper) > 0)) {
            throw new CustomException("有在售商品，不允许删除～");
        }
        //id在ids中 status为0 可以删除
        for (Long id : ids) {
            LambdaQueryWrapper<Dish> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(Dish::getStatus, STATUS_DISABLE_SOLD);
            dishMapper.delete(deleteWrapper);
            LambdaQueryWrapper<DishFlavor> flavorDeleteWrapper = new LambdaQueryWrapper<>();
            flavorDeleteWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorMapper.delete(flavorDeleteWrapper);
        }
    }


    //根据分类查找菜品和口味
    @Override
    public List<Dish> findDish(Long categoryId, Integer status) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, categoryId).eq(Dish::getStatus, STATUS_ENABLE_SOLD);
        List<Dish> dishList = dishMapper.selectList(wrapper);
        if (CollectionUtil.isNotEmpty(dishList)) {
            for (Dish dish : dishList) {
                //根据菜品id查询口味信息
                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(DishFlavor::getDishId, dish.getId());
                List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper1);
                dish.setFlavors(dishFlavorList);
            }
        }
        return dishList;
    }


    //查看菜品分类&名称
    @Override
    public List<Dish> findDish(Long categoryId,String name) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, categoryId)
                .like(StringUtils.isNotEmpty(name),Dish::getName,name);
        //只显示在售
        wrapper. eq(Dish::getStatus, STATUS_ENABLE_SOLD);
        List<Dish> dishList = dishMapper.selectList(wrapper);
        for (Dish dish : dishList) {
            //得到每个菜品, 根据菜品的id从口味表找到当前菜品的口味列表
            LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(DishFlavor::getDishId, dish.getId());
            List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper1);
            dish.setFlavors(dishFlavorList);
        }
        return dishList;
    }
}
