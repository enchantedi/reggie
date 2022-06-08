package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.SetmealService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    //按照name分页查找
    @GetMapping("/setmeal/page")
    public ResultInfo findByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize,
            String name){
        Page<Setmeal> page = setmealService.findByPage(pageNum,pageSize,name);
        return ResultInfo.success(page);
    }

    //保存套餐
    @PostMapping("/setmeal")
    public ResultInfo save(@RequestBody Setmeal setmeal){
        setmealService.save(setmeal);
        return ResultInfo.success(null);
    }

    //删除套餐
    @DeleteMapping("/setmeal")
    public ResultInfo deleteByIds(Long[] ids){
        setmealService.deleteByIds(ids);
        return ResultInfo.success(null);
    }

    //回显套餐信息
    @GetMapping("/setmeal/{id}")
    public ResultInfo findById(@PathVariable("id")Long id){
        Setmeal setmeal = setmealService.findById(id);
        return ResultInfo.success(setmeal);
    }

    //修改套餐
    @PutMapping("/setmeal")
    public ResultInfo update(@RequestBody Setmeal setmeal){
        setmealService.update(setmeal);
        return ResultInfo.success(null);
    }

    //批量起售/停售
    @PostMapping("/setmeal/status/{status}")
    public ResultInfo openAndClose(@PathVariable("status")Integer status,
                                   Long[] ids){
        setmealService.openAndClose(status,ids);
        return ResultInfo.success(null);
    }
}
