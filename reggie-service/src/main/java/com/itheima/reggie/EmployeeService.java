package com.itheima.reggie;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;

import java.util.List;

public interface EmployeeService {
    //登陆
    ResultInfo login(String username, String password);

    //查找员工
    List<Employee> findByName(String name);

    //添加员工
    void save(Employee employee);

    //根据id查找员工
    Employee findById(Long id);

    //修改员工信息
    void update(Employee employee);
}
