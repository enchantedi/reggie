package com.itheima.reggie.controller;

import com.itheima.reggie.CategoryService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    //查找所有菜单
    @GetMapping("/category/list")
    public ResultInfo findAll() {
        List<Category> categoryList = categoryService.findAll();
        return ResultInfo.success(categoryList);
    }
}
