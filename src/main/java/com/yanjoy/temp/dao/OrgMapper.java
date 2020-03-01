package com.yanjoy.temp.dao;

import com.yanjoy.temp.entity.org.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrgMapper {
    Organization selectSingleById(String id);

    List<OrganizationPic> selectPicByOrgId(String id);

    List<Organization> selectChildByParent(Organization organization);

    Organization getRootOrgByProject(String projectId);

    List<Map> getProjects(@Param("projectId") String projectId);

    List<OrganizationSimple> getSimpleOrg(@Param(value = "orgId") String orgId,
                                          @Param(value = "orgType") Integer orgType);

    List<TempOrgTree>getTempOrg(@Param(value = "orgId") String orgId,
                                @Param(value = "orgType") Integer orgType);

    /**
     *获取经理部
     */
    OrgPo getTopOrg(String projectId);

}
