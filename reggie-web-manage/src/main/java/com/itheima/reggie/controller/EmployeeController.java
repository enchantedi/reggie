package com.itheima.reggie.controller;

import com.itheima.reggie.EmployeeService;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HttpSession session;

    //登陆
    @PostMapping("/employee/login")
    public ResultInfo login(@RequestBody Employee employee) {
        String username = employee.getUsername();
        String password = employee.getPassword();
        ResultInfo resultInfo = employeeService.login(username, password);
        //如果登录成功,将返回的Employee对象保存到session
        Employee loginEmployee = (Employee) resultInfo.getData();
        session.setAttribute("SESSION_EMPLOYEE", loginEmployee);
        return resultInfo;
    }

    //退出
    @PostMapping("/employee/logout")
    public ResultInfo logout() {
        //清空session
        session.invalidate();
        return ResultInfo.success(null);
    }

    //查找员工
    @GetMapping("/employee/find")
    public ResultInfo findByName(String name) {
        List<Employee> employeeList = employeeService.findByName(name);
        return ResultInfo.success(employeeList);
    }

    //添加员工
    @PostMapping("/employee")
    public ResultInfo save(@RequestBody Employee employee) {
        employeeService.save(employee);
      return ResultInfo.success(null);
    }

    //根据id查询员工
    @GetMapping("/employee/{id}")
    public ResultInfo findById(@PathVariable("id")Long id){
        Employee employee = employeeService.findById(id);
        return ResultInfo.success(employee);
    }

    //修改员工信息
    @PutMapping("/employee")
    public ResultInfo update(@RequestBody Employee employee){
        employeeService.update(employee);
        return ResultInfo.success(null);
    }
}
