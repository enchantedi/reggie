package com.itheima.reggie.controller;

import com.itheima.reggie.UserService;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.common.SmsTemplate;
import com.itheima.reggie.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import static com.itheima.reggie.domain.User.STATUS_ENABLE;

//客户
@RestController
@Slf4j
public class UserController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private UserService userService;

    //发送验证码
    @PostMapping("/user/sendMsg")
    public ResultInfo sendMessage(@RequestBody User user) {
        String phone = user.getPhone();
        String code = "1";
        //String code = RandomUtil.randomNumbers(6);
        //log.info("验证码{}",code);
        //将手机号对应的验证码保存到Session中
        httpSession.setAttribute("SMS_" + phone, code);
        //smsTemplate.sendSms(phone,code);
        return ResultInfo.success(null);
    }

    //登陆
    @PostMapping("/user/login")
    public ResultInfo login(@RequestBody Map<String, String> info) {
        String phone = info.get("phone");
        String code = info.get("code");
        //验证输入的验证码和保存到session的验证码是不是一个
        String codeFromSession = (String) httpSession.getAttribute("SMS_" + phone);
        if (!StringUtils.equals(code, codeFromSession) || StringUtils.isEmpty(code) || StringUtils.isEmpty(codeFromSession)) {
            throw new CustomException("验证码输入错误！");
        }
        //根据phone查找
        User user = userService.findByPhone(phone);
        //找到了
        if (user != null) {

        } else {
            //没找到
            user = new User();
            user.setPhone(phone);
            user.setStatus(STATUS_ENABLE);
            userService.save(user);
        }
        httpSession.setAttribute("SESSION_USER", user);
        return ResultInfo.success(null);
    }

    //退出登陆
    @PostMapping("/user/logout")
    public ResultInfo logout(){
        httpSession.invalidate();
        return ResultInfo.success(null);
    }
}