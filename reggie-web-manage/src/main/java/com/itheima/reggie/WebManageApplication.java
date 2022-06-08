package com.itheima.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.itheima.reggie.mapper")
@EnableTransactionManagement //SpringBoot 开启声明式事务支持
/**
 * Spring提供了一个@EnableTransactionManagement
 * 注解以在配置类上开启声明式事务的支持。
 * 添加该注解后，Spring容器会自动扫描被@Transactional注解的方法和类。
 */
@Slf4j
public class WebManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebManageApplication.class,args);
        log.info("项目启动成功");
    }
}
