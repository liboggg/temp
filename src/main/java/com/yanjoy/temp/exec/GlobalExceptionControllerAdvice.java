package com.yanjoy.temp.exec;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public void globalExceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        JSONObject object = new JSONObject();
        object.put("code", "1");
        object.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        object.put("data", "服务器异常");
        object.put("msg", e.getMessage());
        try {
            response.getWriter().write(object.toJSONString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}