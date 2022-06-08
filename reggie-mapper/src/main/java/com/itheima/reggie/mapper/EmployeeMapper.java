package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.domain.Employee;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {
}
