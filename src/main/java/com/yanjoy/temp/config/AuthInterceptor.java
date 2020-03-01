package com.yanjoy.temp.config;

import com.alibaba.fastjson.JSONObject;
import com.yanjoy.temp.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisService redisService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        JSONObject object = new JSONObject();
        object.put("code", "0");
        object.put("status", HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            token = "123";
        }
        Object o = redisService.get(token);
        if (Objects.isNull(o)) {
            object.put("status", HttpStatus.FORBIDDEN.value());
            object.put("data", false);
            object.put("msg", "用户未登录，请登录后操作！");
            response.getWriter().write(object.toJSONString());
            return false;
        }
        response.setHeader("token",token);
        object.put("data", true);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}