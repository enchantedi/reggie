package com.itheima.reggie.controller;

import com.itheima.reggie.CategoryService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //查询所有并按照sort正序排序
    @GetMapping("/category/findAll")
    public ResultInfo findAll() {
        List<Category> categoryList = categoryService.findAll();
        return ResultInfo.success(categoryList);
    }

    //添加分类
    @PostMapping("/category")
    public ResultInfo addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return ResultInfo.success(null);
    }

    //修改分类
    @PutMapping("/category")
    public ResultInfo update(@RequestBody Category category) {
        categoryService.update(category);
        return ResultInfo.success(null);
    }

    //删除分类
    @DeleteMapping("/category")
    public ResultInfo deleteById(Long id) {
        categoryService.deleteById(id);
        return ResultInfo.success(null);
    }

    //根据类型查询分类
    @GetMapping("/category/list")
    public ResultInfo findCategoryByType(Integer type){
        List<Category> categoryList = categoryService.findCategoryByType(type);
        return ResultInfo.success(categoryList);
    }
}
