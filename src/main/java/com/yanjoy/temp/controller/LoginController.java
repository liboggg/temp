package com.yanjoy.temp.controller;

import com.alibaba.fastjson.JSONObject;
import com.yanjoy.temp.entity.base.Response;
import com.yanjoy.temp.entity.user.TempUser;
import com.yanjoy.temp.entity.user.UserInfo;
import com.yanjoy.temp.service.LoginService;
import com.yanjoy.temp.service.RedisService;
import com.yanjoy.temp.service.TempUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TempUserService service;

    @Autowired
    private TempUserService userService;


    /**
     * 登录
     *
     * @param id 身份证或电话
     */
    @GetMapping({"/login", "/"})
    public JSONObject login(@NotNull String id) {
        JSONObject response = new JSONObject();
        response.put("code", "0");
        response.put("status", HttpStatus.OK.value());
        String token = UUID.randomUUID().toString();
        boolean login = false;
        try {
            login = loginService.login(id, token);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", "1");
            response.put("msg", e.getMessage());
        }
        response.put("data", login);
        response.put("token", login ? token : null);
        return response;
    }

    /**
     * 登出
     */
    @GetMapping("/login/logout")
    public JSONObject logout(HttpServletRequest request) {
        JSONObject response = new JSONObject();
        response.put("code", "0");
        response.put("status", HttpStatus.OK.value());
        boolean logout = false;
        try {
            logout = loginService.logout(request);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("msg", e.getMessage());
            response.put("code", "1");
        }
        response.put("data", logout);

        return response;
    }

    /**
     * 保存或修改
     */
    @PostMapping(value = "/login/saveOrUpdate")
    public Response saveOrUpdate(@Validated TempUser user, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> allerror = result.getAllErrors();
            String errMsg = "";
            for (ObjectError objectError : allerror) {
                errMsg = objectError.getDefaultMessage();
            }
            return Response.ERROR(false, errMsg);
        }
        int i = 0;
        try {
            i = service.saveOrUpdate(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(false, e.getMessage());
        }
        return Response.SUCCESS(i > 0);
    }

    /**
     * 暂离
     */
    @GetMapping(value = "/login/leave")
    public Response leave(String id) {
        int leave = 0;
        try {
            leave = service.leave(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(false, e.getMessage());
        }
        return Response.SUCCESS(leave > 0);
    }

    @GetMapping(value = "/login/comeback")
    public Response comeBack(String id) {
        int leave = 0;
        try {
            leave = service.comeBack(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(false, e.getMessage());
        }
        return Response.SUCCESS(leave > 0);
    }


    /**
     * 获取平台基本信息
     *
     * @param id 身份证/电话
     * @return 基本信息
     */
    @GetMapping(value = "/login/getPlatUserInfo")
    public Response getPlatUserInfo(@NotNull String id) {
        UserInfo data = new UserInfo();
        try {
            data = service.geuPlatUserInfo(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(data, e.getMessage());
        }
        return Response.SUCCESS(data);
    }


    /**
     * 获取平台基本信息
     *
     * @param id 身份证/电话
     * @return 基本信息
     */
    @GetMapping(value = "/login/checkUser")
    public Response checkUser(@NotNull String id) {
        boolean b = false;
        try {
            b = userService.checkUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(b, e.getMessage());
        }
        if (!b) {
            return Response.SUCCESS_MSG(b, "身份证或电话已注册");
        }
        return Response.SUCCESS(b);
    }

}