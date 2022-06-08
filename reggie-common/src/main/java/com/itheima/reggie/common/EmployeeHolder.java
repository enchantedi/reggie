package com.itheima.reggie.common;

import com.itheima.reggie.domain.Employee;

public class EmployeeHolder {
    private static ThreadLocal<Employee> threadLocal = new ThreadLocal<>();

    //放入Employee
    public static void set(Employee employee) {
        threadLocal.set(employee);
    }

    //获取Employee
    public static Employee get() {
        return threadLocal.get();
    }

    //移除Employee
    public static void remove() {
        threadLocal.remove();
    }
}