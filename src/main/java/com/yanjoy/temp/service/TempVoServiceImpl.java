package com.yanjoy.temp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanjoy.temp.dao.*;
import com.yanjoy.temp.entity.detail.TempDetail;
import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.user.TempUser;
import com.yanjoy.temp.entity.vo.AppTempVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service("tempVoService")
public class TempVoServiceImpl implements TempVoService {

    @Autowired
    private TempVoMapper mapper;

    @Autowired
    private TempMessageMapper messageMapper;

    @Autowired
    private TempEntryMapper entryMapper;

    @Autowired
    private TempDetailMapper detailMapper;

    @Autowired
    private TempUserMapper userMapper;

    @Autowired
    private OrgService orgService;

    @Override
    public List<AppTempVo> appListPage(TempParam param) {
        boolean check = checkList(param);
        if (!check) {
            throw new IllegalArgumentException("the idCard,projectId is not allowed be null");
        }
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return fillAppVoList(param);
    }

    private List<AppTempVo> fillAppVoList(TempParam param) {
        final TempUser tempUser = userMapper.selectByIdCard(param.getIdCard());
        final String name = orgService.getProjects(param.getProjectId()).get(0).get("name").toString();
        return messageMapper.getList(param)
                .stream().map(a -> {
                    AppTempVo obj = new AppTempVo();
                    obj.setUser(tempUser);
                    obj.setTitle(name);
                    obj.setMessage(a);
                    obj.setRoute(entryMapper.selectByPrimaryKey(a.getRoute()));
                    obj.setContactHistory(entryMapper.selectByPrimaryKey(a.getContactHistory()));
                    obj.setAreaHistory(entryMapper.selectByPrimaryKey(a.getAreaHistory()));
                    obj.setPersonHistory(entryMapper.selectByPrimaryKey(a.getPersonHistory()));
                    obj.setHealth(entryMapper.selectByPrimaryKey(a.getHealth()));

                    TempDetail detail = detailMapper.selectByPrimaryKey(a.getDetailId());
                    obj.setTemp(detailMapper.selectByPrimaryKey(detail.getId()));
                    obj.setTempType(detail.getTempType());

                    obj.setBloodTest(entryMapper.selectByPrimaryKey(a.getBloodTest()));
                    obj.setNucleaseTest(entryMapper.selectByPrimaryKey(a.getNucleaseTest()));
                    obj.setWorkStatus(entryMapper.selectByPrimaryKey(a.getWorkStatus()));
                    obj.setLocation(entryMapper.selectByPrimaryKey(a.getLocation()));
                    return obj;
                }).collect(Collectors.toList());
    }

    private boolean checkList(TempParam param) {
        if (StringUtils.isEmpty(param.getProjectId()) || StringUtils.isEmpty(param.getIdCard())) {
            return false;
        }
        return true;
    }


    @Override
    public AppTempVo appSingle(TempParam param) {
        boolean check = checkSingle(param);
        if (!check) {
            throw new IllegalArgumentException("the id,idCard,projectId is not allowed be null");
        }
        return fillAppVoList(param).stream().findFirst().orElse(null);
    }

    private boolean checkSingle(TempParam param) {
        if (StringUtils.isEmpty(param.getProjectId()) || StringUtils.isEmpty(param.getId()) || StringUtils.isEmpty(param.getIdCard())) {
            return false;
        }
        return true;
    }


}
