package com.itheima.reggie.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.CategoryService;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    //查询所有并按照sort正序排序
    @Override
    public List<Category> findAll() {
        return categoryMapper.selectList(null);
    }

    //添加分类
    @Override
    public void addCategory(Category category) {
        categoryMapper.insert(category);
    }

    //修改分类
    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
    }

    //删除分类
    @Override
    public void deleteById(Long id) {
        //查找dish表中的category_id数量
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, id);
        Integer countCategoryId = dishMapper.selectCount(wrapper);
        if (countCategoryId > 0) {
            throw new CustomException("该分类下存在菜品，暂时无法删除!");
        }
        //查找setmeal表中的category_id数量
        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Setmeal::getCategoryId,id);
        Integer count = setmealMapper.selectCount(wrapper1);
        if (count > 0) {
            throw new CustomException("该分类下存在套餐，暂时无法删除!");
        }
        //根据id从category表中删除
        categoryMapper.deleteById(id);
    }

    //根据类型查询分类
    @Override
    public List<Category> findCategoryByType(Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType, type);
        return categoryMapper.selectList(wrapper);
    }
}
