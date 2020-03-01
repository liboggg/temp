package com.yanjoy.temp.controller;

import com.yanjoy.temp.entity.base.Response;
import com.yanjoy.temp.entity.org.Organization;
import com.yanjoy.temp.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/org")
public class OrgController {

    @Autowired
    private OrgService service;

    @GetMapping(value = "/getOrgTree")
    public Response getOrgTree(String projectId) {
        Organization org = null;
        try {
            org = service.getOrgTree(projectId);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(org,e.getMessage());
        }
        return Response.SUCCESS(org);
    }


    @GetMapping(value = "/getProjects")
    public Response getOrgTree() {
        List<Map> org = new ArrayList<>();
        try {
            org = service.getProjects(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(org,e.getMessage());
        }
        return Response.SUCCESS(org);
    }
}
