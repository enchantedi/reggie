package com.itheima.reggie;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Setmeal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//套餐
@Service
@Transactional
public interface SetmealService {

    //按照name分页查找
    Page<Setmeal> findByPage(Integer pageNum,Integer pageSize,String name);

    //保存
    void save(Setmeal setmeal);

    //删除
    void deleteByIds(Long[] ids);

    //回显套餐信息
    Setmeal findById(Long id);

    //修改套餐
    void update(Setmeal setmeal);

    //批量起售/停售
    void openAndClose(Integer status, Long[] ids);

    //查找菜单中的套餐
    List<Setmeal> findSetmeal(Long categoryId, Integer status);
}
