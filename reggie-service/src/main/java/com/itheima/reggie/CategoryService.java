package com.itheima.reggie;

import com.itheima.reggie.domain.Category;

import java.util.List;

public interface CategoryService {
    //查询所有并按照sort正序排序
    List<Category> findAll();

    //添加分类
    void addCategory(Category category);

    //修改分类
    void update(Category category);

    //删除分类
    void deleteById(Long id);

    //根据类型查询分类
    List<Category> findCategoryByType(Integer type);

}
