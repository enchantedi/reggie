package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.itheima.reggie.domain.DishFlavor;
import org.springframework.stereotype.Repository;

@Repository
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
