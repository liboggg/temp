package com.yanjoy.temp.service;

import com.yanjoy.temp.entity.user.TempUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

@Service
public class LoginService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private TempUserService userService;

    private final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public boolean login(String idOrPhone,String token) {
        if(StringUtils.isEmpty(idOrPhone)){
            throw  new IllegalArgumentException("the id is not allowed be null");
        }
        TempUser user = userService.getTempUserByIdCardOrPhone(idOrPhone);
        if (user == null) {
            return false;
        }
        boolean set = redisService.set(token, user.getIdCard());
        if (set) {
            logger.info("用户 {} 登录成功，token是 {}", user.getUserName(), token);
            return true;
        } else {
            logger.info("用户 {} 登录失败", idOrPhone);
            return  true;
        }

    }

    public boolean logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        boolean delete = redisService.delete(token);
        if (!delete) {
            logger.info("token {} 注销失败，请检查是否登录！", token);
            return false;
        }
        logger.info("token {} 注销成功！", token);
        return true;
    }
}