package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.domain.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends BaseMapper<Category> {
    //查询所有并按照sort正序排序
   // List<Category> findAll();

    //添加分类
   // void addCategory(Category category);

    //修改分类
   // void update(Category category);

    //在dish表中查找category_id数量
    // Integer countDishByCategoryId(Long CategoryId);

   // 在setmeal表中查找category_id数量
   // Integer countSetmealByCategoryId(Long CategoryId);

    //根据id删除
    //void delete(Long id);
}
