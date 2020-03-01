package com.yanjoy.temp.service;

import com.yanjoy.temp.dao.OrgMapper;
import com.yanjoy.temp.entity.org.OrgPo;
import com.yanjoy.temp.entity.org.Organization;
import com.yanjoy.temp.entity.org.OrganizationSimple;
import com.yanjoy.temp.entity.org.TempOrgTree;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrgService {

    @Autowired
    private OrgMapper mapper;

    public Organization getOrgTree(String projectId) {
        Organization org = mapper.getRootOrgByProject(projectId);
        return selectTree(org);
    }

    public Organization selectTree(Organization organization) {
        Organization root = mapper.selectSingleById(organization.getId());
        root.setPicList(mapper.selectPicByOrgId(root.getId()));
        if (root.getType() < 5) {
            selectTreeDs(root);
        }
        return root;
    }

    public void selectTreeDs(Organization organization) {
        List<Organization> children = mapper.selectChildByParent(organization);
        if (!children.isEmpty()) {
            organization.setChildren(children);
            for (Organization child : children) {
                child.setPicList(mapper.selectPicByOrgId(child.getId()));
                if (child.getType() < 5) {
                    selectTreeDs(child);
                }
            }
        }
    }

    public List<Map> getProjects(String projectId) {
        return mapper.getProjects(projectId);
    }

    public List<OrganizationSimple> getSimpleOrg(String orgId,
                                                 Integer orgType) {
        return mapper.getSimpleOrg(orgId, orgType);
    }


    public List<TempOrgTree> getTempOrg(String orgId,
                                          Integer orgType) {
        return mapper.getTempOrg(orgId, orgType);
    }

    /**
     * 获取经理部
     */
    OrgPo getTopOrg(String projectId) {
        return mapper.getTopOrg(projectId);
    }

    /**
     * 获取体温报表组织机构树
     */
    public List<TempOrgTree> getTempOrgTree(String orgId, Integer orgType) {
        List<TempOrgTree> three = getThree(new ArrayList<>(), orgId, orgType);
        for (TempOrgTree organizationSimple : three) {
            TempOrgTree obj = new TempOrgTree();
            BeanUtils.copyProperties(organizationSimple,obj);
            obj.setNextChild(new ArrayList<>());
            obj.setOrgName("本部");
            List<TempOrgTree> nextChild = new ArrayList<>();
            nextChild.addAll(organizationSimple.getNextChild());
            nextChild.add(obj);

            organizationSimple.setNextChild(nextChild);
        }

        if(orgType == 2){
            TempOrgTree organizationSimple = getTempOrg(orgId, orgType).stream().findFirst().get();
            List<TempOrgTree> child = new ArrayList<>();
            TempOrgTree obj = new TempOrgTree();
            BeanUtils.copyProperties(organizationSimple,obj);
            child.add(obj);
            organizationSimple.setNextChild(child);
            three.add(organizationSimple);
        }
        return three.stream().sorted(Comparator.comparing(TempOrgTree::getSort)).collect(Collectors.toList());
    }


    private List<TempOrgTree> getThree(List<TempOrgTree> list, String orgId, Integer orgType) {
        orgType = orgType + 1;
        List<TempOrgTree> childThree = getChildThree(orgId, orgType);
        if (orgType < 4) {
            for (TempOrgTree organizationSimple : childThree) {
                organizationSimple.setNextChild(getThree(list, organizationSimple.getOrgId(), orgType));
            }
        }
        return childThree;
    }

    private List<TempOrgTree> getChildThree(String orgId, Integer orgType) {
        return getTempOrg(orgId, orgType);
    }

}
