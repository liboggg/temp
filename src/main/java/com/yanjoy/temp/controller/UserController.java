package com.yanjoy.temp.controller;


import com.yanjoy.temp.entity.base.Response;
import com.yanjoy.temp.entity.user.TempUser;
import com.yanjoy.temp.service.TempUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TempUserService service;

    @GetMapping(value = "/getUser")
    public Response getByIdCardOrPhone(String id) {
        TempUser data = null;
        try {
            data = service.getUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            Response.ERROR(data, e.getMessage());
        }
        return Response.SUCCESS(data);
    }

}
