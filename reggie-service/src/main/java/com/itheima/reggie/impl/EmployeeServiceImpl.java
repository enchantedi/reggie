package com.itheima.reggie.impl;

import cn.hutool.crypto.SecureUtil;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.EmployeeService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    //登陆
    @Override
    public ResultInfo login(String username, String password) {
        //使用username调用mapper查询 username是唯一的
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(username), Employee::getUsername, username);
        Employee employee = employeeMapper.selectOne(wrapper);
        //没有查询到该用户
        if (employee == null) {
            return ResultInfo.error("当前用户不存在");
        }
        //查询到该用户名--加密用户输入的密码,并且和查询回来的密码进行比对
        String encryptionPassword = SecureUtil.md5(password);
        if (!encryptionPassword.equals(employee.getPassword())) {
            return ResultInfo.error("用户名或者密码错误");
        }
        //查看当前用户状态
        if (employee.getStatus() == 0) {
            return ResultInfo.error("当前用户被禁用");
        }
        return ResultInfo.success(employee);
    }

    //查找员工
    @Override
    public List<Employee> findByName(String name) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        List<Employee> employeeList = employeeMapper.selectList(wrapper);
        return employeeList;
    }


    //根据id查找员工
    @Override
    public Employee findById(Long id) {
        return employeeMapper.selectById(id);
    }


    //添加员工
    @Override
    public void save(Employee employee) {
        String md5Password = SecureUtil.md5("123456");
        employee.setPassword(md5Password);
        employee.setStatus(Employee.STATUS_ENABLE);
        employeeMapper.insert(employee);
    }

    //修改员工信息
    @Override
    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }
}
