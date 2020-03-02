package com.yanjoy.temp.service;

import com.yanjoy.temp.dao.TempConfigMapper;
import com.yanjoy.temp.dao.TempDetailMapper;
import com.yanjoy.temp.dao.TempEntryMapper;
import com.yanjoy.temp.dao.TempMessageMapper;
import com.yanjoy.temp.entity.config.TempConfig;
import com.yanjoy.temp.entity.detail.SaveDetailPo;
import com.yanjoy.temp.entity.detail.TempDetail;
import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.entry.TempEntry;
import com.yanjoy.temp.entity.message.TempMessage;
import com.yanjoy.temp.utils.IDNext;
import com.yanjoy.temp.utils.TempTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("tempMonitorService")
public class TempMonitorServiceImpl implements TempMonitorService {

    @Autowired
    private TempMessageMapper mapper;

    @Autowired
    private TempDetailMapper detailMapper;

    @Autowired
    private TempEntryMapper entryMapper;

    @Autowired
    private TempConfigMapper configMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SaveDetailPo saveDetailPo) {
        int i = 0;

        TempMessage message = saveDetailPo.getMessage();
        TempDetail detail = saveDetailPo.getTemp();
        TempEntry route = saveDetailPo.getRoute();
        TempEntry contactHistory = saveDetailPo.getContactHistory();
        TempEntry areaHistory = saveDetailPo.getAreaHistory();
        TempEntry personHistory = saveDetailPo.getPersonHistory();
        TempEntry health = saveDetailPo.getHealth();
        TempEntry bloodTest = saveDetailPo.getBloodTest();
        TempEntry nucleaseTest = saveDetailPo.getNucleaseTest();
        TempEntry workStatus = saveDetailPo.getWorkStatus();
        TempEntry location = saveDetailPo.getLocation();

        String idCard = message.getIdCard();

        String msgUuid = IDNext.uuid();
        String detailUuid = IDNext.uuid();
        String routeUuid = IDNext.uuid();
        String contactHistoryUuid = IDNext.uuid();
        String areaHistoryUuid = IDNext.uuid();
        String personHistoryUuid = IDNext.uuid();
        String healthUuid = IDNext.uuid();
        String bloodUuid = IDNext.uuid();
        String nucleaseUuid = IDNext.uuid();
        String workUuid = IDNext.uuid();
        String locationUuid = IDNext.uuid();

        Date date = new Date();
        String toYMD = TempTimeUtils.dateToYMD(date);
        int stage = checkStage(new Date());
        if (stage == 4) {
            throw new IllegalArgumentException("已超过允许填报时间！");
        }

        //校验该段msg是否存在  存在则都存在不存在则都不存在
        TempMessage check = check(idCard, stage, toYMD);

        message.setCreateDate(date);
        message.setCreateDay(toYMD);
        detail.setCreateDate(date);
        detail.setCreateDay(toYMD);

        route.setCreateDate(date);
        route.setCreateDay(toYMD);
        contactHistory.setCreateDate(date);
        contactHistory.setCreateDay(toYMD);
        areaHistory.setCreateDate(date);
        areaHistory.setCreateDay(toYMD);
        personHistory.setCreateDate(date);
        personHistory.setCreateDay(toYMD);
        health.setCreateDate(date);
        health.setCreateDay(toYMD);

        bloodTest.setCreateDate(date);
        bloodTest.setCreateDay(toYMD);
        nucleaseTest.setCreateDate(date);
        nucleaseTest.setCreateDay(toYMD);
        workStatus.setCreateDate(date);
        workStatus.setCreateDay(toYMD);


        location.setCreateDate(date);
        location.setCreateDay(toYMD);

        detail.setTempType((short) stage);

        //最后条目  其中可能有空
        TempEntry lastRoute = entryMapper.getLastEntryByIdCardAndType(idCard, route.getTypeCode());
        TempEntry lastContact = entryMapper.getLastEntryByIdCardAndType(idCard, contactHistory.getTypeCode());
        TempEntry lastArea = entryMapper.getLastEntryByIdCardAndType(idCard, areaHistory.getTypeCode());
        TempEntry lastPerson = entryMapper.getLastEntryByIdCardAndType(idCard, personHistory.getTypeCode());
        TempEntry lastHealth = entryMapper.getLastEntryByIdCardAndType(idCard, health.getTypeCode());
        TempEntry lastBlood = entryMapper.getLastEntryByIdCardAndType(idCard, bloodTest.getTypeCode());
        TempEntry lastNuclease = entryMapper.getLastEntryByIdCardAndType(idCard, nucleaseTest.getTypeCode());
        TempEntry lastWork = entryMapper.getLastEntryByIdCardAndType(idCard, workStatus.getTypeCode());
        TempEntry lastLocation = entryMapper.getLastEntryByIdCardAndType(idCard, location.getTypeCode());

        route.setWhetherChange(getUpdateStatusChange(lastRoute, route));
        contactHistory.setWhetherChange(getUpdateStatusChange(lastContact, contactHistory));
        areaHistory.setWhetherChange(getUpdateStatusChange(lastArea, areaHistory));
        personHistory.setWhetherChange(getUpdateStatusChange(lastPerson, personHistory));
        health.setWhetherChange(getUpdateStatusChange(lastHealth, health));

        bloodTest.setWhetherChange(getUpdateStatusChange(lastBlood, bloodTest));
        nucleaseTest.setWhetherChange(getUpdateStatusChange(lastNuclease, nucleaseTest));
        workStatus.setWhetherChange(getUpdateStatusChange(lastWork, workStatus));
        location.setWhetherChange(getUpdateStatusChange(lastLocation, location));
        if (null == check) {
            //msg 不存在 全插入
            //save
            message.setId(msgUuid);
            message.setDetailId(detailUuid);
            message.setRoute(routeUuid);
            message.setContactHistory(contactHistoryUuid);
            message.setAreaHistory(areaHistoryUuid);
            message.setPersonHistory(personHistoryUuid);
            message.setHealth(healthUuid);
            message.setBloodTest(bloodUuid);
            message.setNucleaseTest(nucleaseUuid);
            message.setWorkStatus(workUuid);
            message.setLocation(locationUuid);

            i += saveMsg(message);

            detail.setId(detailUuid);
            detail.setMessageId(msgUuid);
            i += saveDetail(detail);

            route.setId(routeUuid);
            route.setMessageId(msgUuid);
            i += saveEntry(route);

            contactHistory.setId(contactHistoryUuid);
            contactHistory.setMessageId(msgUuid);
            i += saveEntry(contactHistory);

            areaHistory.setId(areaHistoryUuid);
            areaHistory.setMessageId(msgUuid);
            i += saveEntry(areaHistory);

            personHistory.setId(personHistoryUuid);
            personHistory.setMessageId(msgUuid);
            i += saveEntry(personHistory);


            health.setId(healthUuid);
            health.setMessageId(msgUuid);
            i += saveEntry(health);

            bloodTest.setId(bloodUuid);
            bloodTest.setMessageId(msgUuid);
            i += saveEntry(bloodTest);

            nucleaseTest.setId(nucleaseUuid);
            nucleaseTest.setMessageId(msgUuid);
            i += saveEntry(nucleaseTest);

            workStatus.setId(workUuid);
            workStatus.setMessageId(msgUuid);
            i += saveEntry(workStatus);

            location.setId(locationUuid);
            location.setMessageId(msgUuid);
            i += saveEntry(location);

        } else {
            //msg存在  但是其中某项可能为空
            if (StringUtils.isEmpty(check.getRoute())) {
                //save
                route.setId(routeUuid);
                route.setMessageId(msgUuid);
                i += saveEntry(route);
            } else {
                //update
                route.setId(check.getRoute());
                route.setMessageId(check.getId());
                i += updateEntry(route);
                routeUuid = check.getRoute();
            }

            if (StringUtils.isEmpty(check.getContactHistory())) {
                contactHistory.setId(contactHistoryUuid);
                contactHistory.setMessageId(msgUuid);
                i += saveEntry(contactHistory);
            } else {
                contactHistory.setId(check.getContactHistory());
                contactHistory.setMessageId(check.getId());
                i += updateEntry(contactHistory);
                contactHistoryUuid = check.getContactHistory();
            }

            if (StringUtils.isEmpty(check.getAreaHistory())) {
                areaHistory.setId(areaHistoryUuid);
                areaHistory.setMessageId(msgUuid);
                i += saveEntry(areaHistory);
            } else {
                areaHistory.setId(check.getAreaHistory());
                areaHistory.setMessageId(check.getId());
                i += updateEntry(areaHistory);
                areaHistoryUuid = check.getAreaHistory();
            }

            if (StringUtils.isEmpty(check.getPersonHistory())) {
                personHistory.setId(personHistoryUuid);
                personHistory.setMessageId(msgUuid);
                i += saveEntry(contactHistory);
            } else {
                personHistory.setId(check.getPersonHistory());
                personHistory.setMessageId(check.getId());
                i += updateEntry(personHistory);
                personHistoryUuid = check.getPersonHistory();
            }

            if (StringUtils.isEmpty(check.getHealth())) {
                health.setId(healthUuid);
                health.setMessageId(msgUuid);
                i += saveEntry(health);
            } else {
                health.setId(check.getHealth());
                health.setMessageId(check.getId());
                i += updateEntry(health);
                healthUuid = check.getHealth();
            }

            if (StringUtils.isEmpty(check.getBloodTest())) {
                bloodTest.setId(bloodUuid);
                bloodTest.setMessageId(msgUuid);
                i += saveEntry(bloodTest);
            } else {
                bloodTest.setId(check.getBloodTest());
                bloodTest.setMessageId(check.getId());
                i += updateEntry(bloodTest);
                bloodUuid = check.getBloodTest();
            }

            if (StringUtils.isEmpty(check.getNucleaseTest())) {
                nucleaseTest.setId(nucleaseUuid);
                nucleaseTest.setMessageId(msgUuid);
                i += saveEntry(nucleaseTest);
            } else {
                nucleaseTest.setId(check.getNucleaseTest());
                nucleaseTest.setMessageId(check.getId());
                i += updateEntry(nucleaseTest);
                nucleaseUuid = check.getNucleaseTest();
            }

            if (StringUtils.isEmpty(check.getWorkStatus())) {
                workStatus.setId(workUuid);
                workStatus.setMessageId(msgUuid);
                i += saveEntry(workStatus);
            } else {
                workStatus.setId(check.getWorkStatus());
                workStatus.setMessageId(check.getId());
                i += updateEntry(workStatus);
                workUuid = check.getWorkStatus();
            }

            if (StringUtils.isEmpty(check.getLocation())) {
                location.setId(locationUuid);
                location.setMessageId(msgUuid);
                i += saveEntry(location);
            } else {
                location.setId(check.getLocation());
                location.setMessageId(check.getId());
                i += updateEntry(workStatus);
                locationUuid = check.getLocation();
            }


            detail.setId(check.getDetailId());
            detail.setMessageId(check.getId());
            i += updateDetail(detail);


            message.setId(check.getId());
            message.setDetailId(check.getDetailId());
            message.setRoute(routeUuid);
            message.setContactHistory(contactHistoryUuid);
            message.setAreaHistory(areaHistoryUuid);
            message.setPersonHistory(personHistoryUuid);
            message.setHealth(healthUuid);
            message.setBloodTest(bloodUuid);
            message.setNucleaseTest(nucleaseUuid);
            message.setWorkStatus(workUuid);
            message.setLocation(locationUuid);
            i += updateMsg(message);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveConfig(TempConfig config) {
        int i = 0;
        if (config.getUpperLimit() == null) {
            return i;
        }
        configMapper.delete();
        config.setId(IDNext.uuid());
        i = configMapper.insert(config);
        return i;
    }

    @Override
    public BigDecimal getConfig() {
        return configMapper.getTempConfig();
    }


    /**
     * 判断entry要修改状态下 旧的entry 与新的entry 状态是否应该修改
     *
     * @param old 旧数据
     * @param now 新数据
     * @return 相同返回0  不相同返回1
     */
    private short getUpdateStatusChange(TempEntry old, TempEntry now) {
        //无记录则不变化
        if (old == null) {
            return 0;
        }
        //所有相同  == 0
        if ((old.getStatus() + old.getName() + old.getTypeCode() + old.getTestResult() + "-").equals(
                now.getStatus() + now.getName() + now.getTypeCode() + now.getTestResult() + "-")) {
            return 0;
        } else {
            return 1;
        }
    }


    /**
     * 校验此时阶段是否存在数据
     *
     * @param idCard 身份证
     * @param stage  阶段
     * @param toYMD  年月日
     * @return 不存在 null 存在 TempMessage
     */
    private TempMessage check(String idCard, int stage, String toYMD) {
        TempParam tempParam = new TempParam();
        tempParam.setIdCard(idCard);
        tempParam.setDateDay(toYMD);
        tempParam.setTempType(stage);

        List<TempDetail> list = detailMapper.getList(tempParam);
        if (list.isEmpty()) {
            return null;
        } else {
            return mapper.selectByPrimaryKey(list.stream().findFirst().get().getMessageId());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public int saveDetail(TempDetail detail) {
        return detailMapper.insertSelective(detail);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateDetail(TempDetail detail) {
        return detailMapper.updateByPrimaryKeySelective(detail);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateMsg(TempMessage msg) {
        return mapper.updateByPrimaryKeySelective(msg);
    }

    @Transactional(rollbackFor = Exception.class)
    public int saveMsg(TempMessage msg) {
        return mapper.insertSelective(msg);
    }

    /**
     * 校验今日detail
     *
     * @param id    messageId
     * @param stage 阶段
     * @return 存在返回detailId 不存在返回null
     */
    private String checkDetail(String id, int stage) {
        TempParam tempParam = new TempParam();
        tempParam.setMessageId(id);
        tempParam.setTempType(stage);
        List<TempDetail> list = detailMapper.getList(tempParam);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.stream().findFirst().get().getId();
        }
    }


    /**
     * 检验今日Message
     *
     * @param idCard 身份证
     * @param date   年月日
     * @return 若存在返回id 不存在返回null
     */
    private String checkMessage(String idCard, String date) {
        TempParam tempParam = new TempParam();
        tempParam.setIdCard(idCard);
        tempParam.setDateDay(date);
        List<TempMessage> list = mapper.getList(tempParam);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.stream().findFirst().get().getId();
        }
    }

    @Transactional
    public int saveEntry(TempEntry tempEntry) {
        return entryMapper.insertSelective(tempEntry);
    }

    @Transactional
    public int updateEntry(TempEntry tempEntry) {
        return entryMapper.updateByPrimaryKeySelective(tempEntry);
    }

    /**
     * 根据当前时间返回体温阶段
     * 1 上午7时
     * 2 中午12时
     * 3 下午5时
     * 4 不允许修改添加
     */
    private int checkStage(Date date) {
        return TempTimeUtils.getTempStage(date);
    }


}
