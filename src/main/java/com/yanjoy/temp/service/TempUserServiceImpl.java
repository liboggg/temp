package com.yanjoy.temp.service;

import com.yanjoy.temp.dao.TempDetailMapper;
import com.yanjoy.temp.dao.TempUserMapper;
import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.user.TempUser;
import com.yanjoy.temp.entity.user.UserInfo;
import com.yanjoy.temp.utils.IDNext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("tempUserService")
public class TempUserServiceImpl implements TempUserService {

    @Autowired
    private TempUserMapper mapper;

    @Autowired
    private TempDetailMapper detailMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdate(TempUser tempUser) {
        int i = 0;
        if (null == tempUser) {
            return i;
        }


        TempUser idCardOlg = mapper.selectByIdCard(tempUser.getIdCard());

        TempUser phoneOld = mapper.selectByIdCard(tempUser.getPhone());

        if (null == idCardOlg && null == phoneOld) {
            //save
            tempUser.setId(IDNext.uuid());
            i = save(tempUser);
            //身份证注册 电话未注册
        } else if (idCardOlg != null && phoneOld == null) {
            //update
            tempUser.setId(idCardOlg.getId());
            i = update(tempUser);
            //身份证未注册 电话注册
        } else if (idCardOlg == null && phoneOld != null) {
            throw new IllegalArgumentException("该手机号已被注册！");
            //都被注册
        } else {
            //同一人
            if (idCardOlg.getIdCard().equals(phoneOld.getIdCard())) {
                //update
                tempUser.setId(idCardOlg.getId());
                i = update(tempUser);
            } else {
                throw new IllegalArgumentException("该手机号已被注册！");
            }
        }
        return i;
    }


    @Transactional(rollbackFor = Exception.class)
    public int save(TempUser tempUser) {
        return mapper.insertSelective(tempUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public int update(TempUser tempUser) {
        return mapper.updateByPrimaryKeySelective(tempUser);
    }


    @Override
    public TempUser getTempUserByIdCardOrPhone(String idCard) {
        return mapper.selectByIdCard(idCard);
    }

    @Override
    public TempUser getUser(String idCardOrPhone) {
        TempUser tempUser = mapper.selectByIdCard(idCardOrPhone);
        if (tempUser != null) {
            TempParam param = new TempParam();
            param.setIdCard(tempUser.getIdCard());
            tempUser.setAmount(detailMapper.getList(param).size());
        }
        return tempUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int leave(String idCard) {
        if (StringUtils.isEmpty(idCard)) {
            throw new IllegalArgumentException("the idCard is not allowed be null");
        }
        return mapper.updateLeave(idCard);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int comeBack(String idCard) {
        if (StringUtils.isEmpty(idCard)) {
            throw new IllegalArgumentException("the idCard is not allowed be null");
        }
        return mapper.updateComeBack(idCard);
    }

    @Override
    public UserInfo geuPlatUserInfo(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("the id is not allowed be null");
        }
        return mapper.getPlatUserInfo(id).stream().findFirst().orElse(null);
    }

    @Override
    public boolean checkUser(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("the id is not allowed be null");
        }
        TempUser old = mapper.selectByIdCard(id);
        if (old == null) {
            return true;
        } else {
            return false;
        }
    }


}