package com.itheima.reggie.impl;

import cn.hutool.bloomfilter.filter.SDBMFilter;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.SetmealService;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.domain.SetmealDish;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.SetmealDishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.itheima.reggie.domain.Setmeal.STATUS_DISABLE_SOLD;
import static com.itheima.reggie.domain.Setmeal.STATUS_ENABLE_SOLD;

//套餐
@Service
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    //按照name分页查找
    @Override
    public Page<Setmeal> findByPage(Integer pageNum, Integer pageSize, String name) {
        //在setmeal表中查询条件
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        //查询
        Page page = setmealMapper.selectPage(new Page(pageNum, pageSize), wrapper);
        //查询结果
        List<Setmeal> pageRecords = page.getRecords();
        //遍历
        if (CollectionUtil.isNotEmpty(pageRecords)) {
            for (Setmeal pageRecord : pageRecords) {
                //根据CategoryId去查找CategoryName并赋给setmeal表中字段
                Category category = categoryMapper.selectById(pageRecord.getCategoryId());
                pageRecord.setCategoryName(category.getName());
                //当前套餐对应菜品信息
                LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(SetmealDish::getSetmealId, pageRecord.getId());
                List<SetmealDish> setmealDishList = setmealDishMapper.selectList(wrapper1);
                pageRecord.setSetmealDishes(setmealDishList);
            }
        }
        return page;
    }

    //保存
    @Override
    public void save(Setmeal setmeal) {
        setmealMapper.insert(setmeal);
        List<SetmealDish> setmealDishList = setmeal.getSetmealDishes();
        if (CollectionUtil.isNotEmpty(setmealDishList)) {
            for (SetmealDish setmealDish : setmealDishList) {
                //设置套餐id
                setmealDish.setSetmealId(setmeal.getId());
                setmealDishMapper.insert(setmealDish);
            }
        }
    }

    //删除
    @Override
    public void deleteByIds(Long[] ids) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getStatus, STATUS_ENABLE_SOLD).in(Setmeal::getId, ids);
        Integer count = setmealMapper.selectCount(wrapper);
        if (count > 0) {
            throw new CustomException("当前套餐处于在售状态，不允许删除!");
        } else {
            //删除套餐表
            LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Setmeal::getStatus,STATUS_DISABLE_SOLD).in(Setmeal::getId,ids);
            setmealMapper.delete(wrapper1);
            //删除中间表
            LambdaQueryWrapper<SetmealDish> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.in(SetmealDish::getSetmealId,ids);
            setmealDishMapper.delete(wrapper2);
        }
    }

    //回显套餐信息
    @Override
    public Setmeal findById(Long id) {
        //查找套餐信息
        Setmeal setmeal = setmealMapper.selectById(id);
        //查看菜品信息
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishMapper.selectList(wrapper);
        setmeal.setSetmealDishes(setmealDishList);
        return setmeal;
    }

    //修改套餐
    @Override
    public void update(Setmeal setmeal) {
        //修改套餐表信息
        setmealMapper.updateById(setmeal);
        //修改中间表信息
        //按照setmeal的id全部删掉
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getDishId, setmeal.getId());
        setmealDishMapper.delete(wrapper);
        //按照传过来的值添加
        List<SetmealDish> setmealDishList = setmeal.getSetmealDishes();
        if (CollectionUtil.isNotEmpty(setmealDishList)) {
            for (SetmealDish setmealDish : setmealDishList) {
                //设置setmealDish的SetmealId
                setmealDish.setSetmealId(setmeal.getId());
                setmealDishMapper.insert(setmealDish);
            }
        }
    }

    //批量起售/停售
    @Override
    public void openAndClose(Integer status, Long[] ids) {
        if (ids != null && ids.length > 0) {
            LambdaUpdateWrapper<Setmeal> wrapper = new LambdaUpdateWrapper<>();
            for (Long id : ids) {
                wrapper.eq(Setmeal::getId, id).set(Setmeal::getStatus, status);
                setmealMapper.update(null, wrapper);
            }
        }
    }

    //查找菜单中的套餐
    @Override
    public List<Setmeal> findSetmeal(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,categoryId).eq(Setmeal::getStatus, Dish.STATUS_ENABLE_SOLD);
        return setmealMapper.selectList(wrapper);
    }
}

