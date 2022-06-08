package com.itheima.reggie.handler;

import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    //处理唯一值重复的异常
    @ExceptionHandler(DuplicateKeyException.class)
    public ResultInfo handleDuplicateKeyException(Exception e) {
        e.printStackTrace();
        if (e.getMessage().contains("idx_category_name")) {
            return ResultInfo.error("分类名称的值重复");
        }
        //给前端提示
        return ResultInfo.error("填写的值重复");
    }

    //处理自定义异常
    @ExceptionHandler(CustomException.class)
    public ResultInfo handleCustomException(Exception e) {
        e.printStackTrace();
        if (e.getMessage().contains("SignatureVerificationException")) {
            return ResultInfo.error("无效签名");
        } else if (e.getMessage().contains("TokenExpiredException")) {
            return ResultInfo.error("签名过期");
        } else if (e.getMessage().contains("AlgorithmMismatchException")) {
            return ResultInfo.error("算法不一致");
        }
        //给前端提示
        return ResultInfo.error(e.getMessage());
    }

    //非预期异常
    @ExceptionHandler(Exception.class)
    public ResultInfo handleException(Exception e) {
        e.printStackTrace();
        return ResultInfo.error("服务器开小差了,请联系管理员");
    }
}


