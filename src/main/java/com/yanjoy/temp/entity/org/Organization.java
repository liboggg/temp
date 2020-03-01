package com.yanjoy.temp.entity.org;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yanjoy.temp.entity.org.BaseEntity;
import com.yanjoy.temp.entity.org.OrganizationPic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * 组织机构实体Bean.
 *
 * @ClassName: Organization
 * @Description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization extends BaseEntity {


    /**
     * The project id.
     */
    //项目ID
    private String projectId;

    /**
     * 名称拼音.
     */
    private String namePhonetic;

    /**
     * 区段编号.
     */
    private String zoneCode;


    /**
     * The des.
     */
    // 描述
    private String des;

    /**
     * The parent id.
     */
    // 父级
    private String parentId;

    /**
     * The parent name.
     */
    private String parentName;

    /**
     * The organization id.
     */
    //集中加工厂ID
    private String organizationId;

    /**
     * The second company name.
     */
    //二级公司名称
    private String secondCompanyName;

    /**
     * The third company name.
     */
    //三级公司名称
    private String thirdCompanyName;

    /**
     * The adress.
     */
    // 地址
    private String adress;

    /**
     * The marker.
     */
    // 坐标
    private String marker;

    /**
     * The tree level.
     */
    // 级数
    private Integer treeLevel;

    /**
     * The sort.
     */
    // 排序
    private Integer sort = 0;

    /**
     * The city id.
     */
    //城市
    private String cityId;

    /**
     * The city type.
     */
    private String cityType;

    /**
     * The city code.
     */
    private String cityCode;

    /**
     * The industry id.
     */
    //行业
    private String industryId;

    /**
     * The industry type.
     */
    private String industryType;

    /**
     * The industry code.
     */
    private String industryCode;

    /**
     * The industry number.
     */
    private String industryNumber;

    /**
     * The work point id.
     */
    //工点
    private String workPointId;

    /**
     * The work point type.
     */
    private String workPointType;

    /**
     * The work point code.
     */
    private String workPointCode;

    /**
     * The work point number.
     */
    private String workPointNumber;

    /**
     * The specialty id.
     */
    //专业
    private String specialtyId;

    /**
     * The specialty type.
     */
    private String specialtyType;

    /**
     * The specialty code.
     */
    private String specialtyCode;

    /**
     * The specialty number.
     */
    private String specialtyNumber;

    /**
     * The position id.
     */
    //部位
    private String positionId;

    /**
     * The position type.
     */
    private String positionType;

    /**
     * The position code.
     */
    private String positionCode;

    /**
     * The position number.
     */
    private String positionNumber;

    /**
     * The position level.
     */
    //层数
    private String positionLevel;


    /**
     * The province value.
     */
    //省.市.区
    private String provinceValue;

    /**
     * The province name.
     */
    private String provinceName;

    /**
     * The city value.
     */
    private String cityValue;

    /**
     * The city name.
     */
    private String cityName;

    /**
     * The area value.
     */
    private String areaValue;

    /**
     * The area name.
     */
    private String areaName;

    /**
     * The project type value.
     */
    private String projectTypeValue;

    /**
     * The bim id.
     */
    private String bimId;

    /**
     * The has B 3 D.
     */
    private Long hasB3D;

    /**
     * The type.
     */
    //组织机构类型 1.总包  2.总包部 3.分部 4.工区 5.工点 6.专业 7.集中加工厂
    private Integer type;

    //1.站点,2.管廊
    private Integer stationType;

    /**
     * 工程类型.
     */
    private String projectType;

    /**
     * The organization full name.
     */
    private String organizationFullName;

    /**
     * The organization full id.
     */
    private String organizationFullId;

    /**
     * The attachment id.
     */
    private String attachmentId;

    /**
     * The is use.
     */
    private String isUse;

    /**
     * The total color value.
     */
    private String totalColorValue;

    //全景url
    private String panorama;

    //隐患排查开始时间
    private Date inspectStartDate;

    List<OrganizationPic> picList = new ArrayList<>();

    //是否换乘站
    private Boolean changeStation = false;

    //是否停工
    private Boolean stopStatus = false;

    //停工时限
    private Date stopStartDate;
    private Date stopEndDate;
    //停工原因
    private String stopMemo;

    //联系人
    private String linkman;
    private String linkmanTel;


    //大屏是否上传模型 1.是，0.否
    private Integer hasWhole;   //是否上传整体模型
    private Integer hasBIM;     //是否上传BIM模型
    private Integer hasScene;   //是否上传实景模型
    private Integer hasConduit;   //是否上传管道模型
    private Integer hasGeology;   //是否上传地质模型

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkmanTel() {
        return linkmanTel;
    }

    public void setLinkmanTel(String linkmanTel) {
        this.linkmanTel = linkmanTel;
    }

    /**
     * Gets the checks if is use.
     *
     * @return the checks if is use
     */
    public String getIsUse() {
        return isUse;
    }

    /**
     * Sets the checks if is use.
     *
     * @param isUse the new checks if is use
     */
    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    /**
     * Gets the total color value.
     *
     * @return the total color value
     */
    public String getTotalColorValue() {
        return totalColorValue;
    }

    /**
     * Sets the total color value.
     *
     * @param totalColorValue the new total color value
     */
    public void setTotalColorValue(String totalColorValue) {
        this.totalColorValue = totalColorValue;
    }


    /**
     * The children.
     */
    private List<Organization> children;

    /**
     * Gets the project id.
     *
     * @return the project id
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Sets the project id.
     *
     * @param projectId the new project id
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

//	public String getBimStructureId() {
//		return bimStructureId;
//	}
//
//	public void setBimStructureId(String bimStructureId) {
//		this.bimStructureId = bimStructureId;
//	}


    /**
     * Gets the project type value.
     *
     * @return the project type value
     */
    public String getProjectTypeValue() {
        return projectTypeValue;
    }

    /**
     * Sets the project type value.
     *
     * @param projectTypeValue the new project type value
     */
    public void setProjectTypeValue(String projectTypeValue) {
        this.projectTypeValue = projectTypeValue;
    }

    /**
     * Gets the name phonetic.
     *
     * @return the name phonetic
     */
    public String getNamePhonetic() {
        return namePhonetic;
    }

    /**
     * Sets the name phonetic.
     *
     * @param namePhonetic the new name phonetic
     */
    public void setNamePhonetic(String namePhonetic) {
        this.namePhonetic = namePhonetic;
    }

    /**
     * Gets the zone code.
     *
     * @return the zone code
     */
    public String getZoneCode() {
        return zoneCode;
    }

    /**
     * Sets the zone code.
     *
     * @param zoneCode the new zone code
     */
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    /**
     * Gets the project type.
     *
     * @return the project type
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * Sets the project type.
     *
     * @param projectType the new project type
     */
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    /**
     * Gets the organization full name.
     *
     * @return the organization full name
     */
    public String getOrganizationFullName() {
        return organizationFullName;
    }

    /**
     * Sets the organization full name.
     *
     * @param organizationFullName the new organization full name
     */
    public void setOrganizationFullName(String organizationFullName) {
        this.organizationFullName = organizationFullName;
    }

    /**
     * Gets the organization full id.
     *
     * @return the organization full id
     */
    public String getOrganizationFullId() {
        return organizationFullId;
    }

    /**
     * Sets the organization full id.
     *
     * @param organizationFullId the new organization full id
     */
    public void setOrganizationFullId(String organizationFullId) {
        this.organizationFullId = organizationFullId;
    }

    /**
     * Gets the des.
     *
     * @return the des
     */
    public String getDes() {
        return des;
    }

    /**
     * Sets the des.
     *
     * @param des the new des
     */
    public void setDes(String des) {
        this.des = des;
    }

    /**
     * Gets the parent id.
     *
     * @return the parent id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the parent id.
     *
     * @param parentId the new parent id
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * Gets the parent name.
     *
     * @return the parent name
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * Sets the parent name.
     *
     * @param parentName the new parent name
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * Gets the organization id.
     *
     * @return the organization id
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * Sets the organization id.
     *
     * @param organizationId the new organization id
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * Gets the second company name.
     *
     * @return the second company name
     */
    public String getSecondCompanyName() {
        return secondCompanyName;
    }

    /**
     * Sets the second company name.
     *
     * @param secondCompanyName the new second company name
     */
    public void setSecondCompanyName(String secondCompanyName) {
        this.secondCompanyName = secondCompanyName;
    }

    /**
     * Gets the third company name.
     *
     * @return the third company name
     */
    public String getThirdCompanyName() {
        return thirdCompanyName;
    }

    /**
     * Sets the third company name.
     *
     * @param thirdCompanyName the new third company name
     */
    public void setThirdCompanyName(String thirdCompanyName) {
        this.thirdCompanyName = thirdCompanyName;
    }

    /**
     * Gets the adress.
     *
     * @return the adress
     */
    public String getAdress() {
        return adress;
    }

    /**
     * Sets the adress.
     *
     * @param adress the new adress
     */
    public void setAdress(String adress) {
        this.adress = adress;
    }

    /**
     * Gets the marker.
     *
     * @return the marker
     */
    public String getMarker() {
        return marker;
    }

    /**
     * Sets the marker.
     *
     * @param marker the new marker
     */
    public void setMarker(String marker) {
        this.marker = marker;
    }

    /**
     * Gets the tree level.
     *
     * @return the tree level
     */
    public Integer getTreeLevel() {
        return treeLevel;
    }

    /**
     * Sets the tree level.
     *
     * @param treeLevel the new tree level
     */
    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    /**
     * Gets the sort.
     *
     * @return the sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * Sets the sort.
     *
     * @param sort the new sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStationType() {
        return stationType;
    }

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    /**
     * Gets the children.
     *
     * @return the children
     */
    public List<Organization> getChildren() {
        return children;
    }

    /**
     * Sets the children.
     *
     * @param children the new children
     */
    public void setChildren(List<Organization> children) {
        this.children = children;
    }

    /**
     * Gets the city id.
     *
     * @return the city id
     */
    public String getCityId() {
        return cityId;
    }

    /**
     * Sets the city id.
     *
     * @param cityId the new city id
     */
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     * Gets the city type.
     *
     * @return the city type
     */
    public String getCityType() {
        return cityType;
    }

    /**
     * Sets the city type.
     *
     * @param cityType the new city type
     */
    public void setCityType(String cityType) {
        this.cityType = cityType;
    }

    /**
     * Gets the city code.
     *
     * @return the city code
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * Sets the city code.
     *
     * @param cityCode the new city code
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * Gets the industry id.
     *
     * @return the industry id
     */
    public String getIndustryId() {
        return industryId;
    }

    /**
     * Sets the industry id.
     *
     * @param industryId the new industry id
     */
    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    /**
     * Gets the industry type.
     *
     * @return the industry type
     */
    public String getIndustryType() {
        return industryType;
    }

    /**
     * Sets the industry type.
     *
     * @param industryType the new industry type
     */
    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    /**
     * Gets the industry code.
     *
     * @return the industry code
     */
    public String getIndustryCode() {
        return industryCode;
    }

    /**
     * Sets the industry code.
     *
     * @param industryCode the new industry code
     */
    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    /**
     * Gets the industry number.
     *
     * @return the industry number
     */
    public String getIndustryNumber() {
        return industryNumber;
    }

    /**
     * Sets the industry number.
     *
     * @param industryNumber the new industry number
     */
    public void setIndustryNumber(String industryNumber) {
        this.industryNumber = industryNumber;
    }

    /**
     * Gets the work point id.
     *
     * @return the work point id
     */
    public String getWorkPointId() {
        return workPointId;
    }

    /**
     * Sets the work point id.
     *
     * @param workPointId the new work point id
     */
    public void setWorkPointId(String workPointId) {
        this.workPointId = workPointId;
    }

    /**
     * Gets the work point type.
     *
     * @return the work point type
     */
    public String getWorkPointType() {
        return workPointType;
    }

    /**
     * Sets the work point type.
     *
     * @param workPointType the new work point type
     */
    public void setWorkPointType(String workPointType) {
        this.workPointType = workPointType;
    }

    /**
     * Gets the work point code.
     *
     * @return the work point code
     */
    public String getWorkPointCode() {
        return workPointCode;
    }

    /**
     * Sets the work point code.
     *
     * @param workPointCode the new work point code
     */
    public void setWorkPointCode(String workPointCode) {
        this.workPointCode = workPointCode;
    }

    /**
     * Gets the work point number.
     *
     * @return the work point number
     */
    public String getWorkPointNumber() {
        return workPointNumber;
    }

    /**
     * Sets the work point number.
     *
     * @param workPointNumber the new work point number
     */
    public void setWorkPointNumber(String workPointNumber) {
        this.workPointNumber = workPointNumber;
    }

    /**
     * Gets the specialty id.
     *
     * @return the specialty id
     */
    public String getSpecialtyId() {
        return specialtyId;
    }

    /**
     * Sets the specialty id.
     *
     * @param specialtyId the new specialty id
     */
    public void setSpecialtyId(String specialtyId) {
        this.specialtyId = specialtyId;
    }

    /**
     * Gets the specialty type.
     *
     * @return the specialty type
     */
    public String getSpecialtyType() {
        return specialtyType;
    }

    /**
     * Sets the specialty type.
     *
     * @param specialtyType the new specialty type
     */
    public void setSpecialtyType(String specialtyType) {
        this.specialtyType = specialtyType;
    }

    /**
     * Gets the specialty code.
     *
     * @return the specialty code
     */
    public String getSpecialtyCode() {
        return specialtyCode;
    }

    /**
     * Sets the specialty code.
     *
     * @param specialtyCode the new specialty code
     */
    public void setSpecialtyCode(String specialtyCode) {
        this.specialtyCode = specialtyCode;
    }

    /**
     * Gets the specialty number.
     *
     * @return the specialty number
     */
    public String getSpecialtyNumber() {
        return specialtyNumber;
    }

    /**
     * Sets the specialty number.
     *
     * @param specialtyNumber the new specialty number
     */
    public void setSpecialtyNumber(String specialtyNumber) {
        this.specialtyNumber = specialtyNumber;
    }

    /**
     * Gets the bim id.
     *
     * @return the bim id
     */
    public String getBimId() {
        return bimId;
    }

    /**
     * Sets the bim id.
     *
     * @param bimId the new bim id
     */
    public void setBimId(String bimId) {
        this.bimId = bimId;
    }

    /**
     * Gets the checks for B 3 D.
     *
     * @return the checks for B 3 D
     */
    public Long getHasB3D() {
        return hasB3D;
    }

    /**
     * Sets the checks for B 3 D.
     *
     * @param hasB3D the new checks for B 3 D
     */
    public void setHasB3D(Long hasB3D) {
        this.hasB3D = hasB3D;
    }

    /**
     * Gets the province value.
     *
     * @return the province value
     */
    public String getProvinceValue() {
        return provinceValue;
    }

    /**
     * Sets the province value.
     *
     * @param provinceValue the new province value
     */
    public void setProvinceValue(String provinceValue) {
        this.provinceValue = provinceValue;
    }

    /**
     * Gets the city value.
     *
     * @return the city value
     */
    public String getCityValue() {
        return cityValue;
    }

    /**
     * Sets the city value.
     *
     * @param cityValue the new city value
     */
    public void setCityValue(String cityValue) {
        this.cityValue = cityValue;
    }

    /**
     * Gets the area value.
     *
     * @return the area value
     */
    public String getAreaValue() {
        return areaValue;
    }

    /**
     * Sets the area value.
     *
     * @param areaValue the new area value
     */
    public void setAreaValue(String areaValue) {
        this.areaValue = areaValue;
    }

    /**
     * Gets the province name.
     *
     * @return the province name
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * Sets the province name.
     *
     * @param provinceName the new province name
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * Gets the city name.
     *
     * @return the city name
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Sets the city name.
     *
     * @param cityName the new city name
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Gets the area name.
     *
     * @return the area name
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * Sets the area name.
     *
     * @param areaName the new area name
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /**
     * Gets the position id.
     *
     * @return the position id
     */
    public String getPositionId() {
        return positionId;
    }

    /**
     * Sets the position id.
     *
     * @param positionId the new position id
     */
    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    /**
     * Gets the position type.
     *
     * @return the position type
     */
    public String getPositionType() {
        return positionType;
    }

    /**
     * Sets the position type.
     *
     * @param positionType the new position type
     */
    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    /**
     * Gets the position code.
     *
     * @return the position code
     */
    public String getPositionCode() {
        return positionCode;
    }

    /**
     * Sets the position code.
     *
     * @param positionCode the new position code
     */
    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    /**
     * Gets the position number.
     *
     * @return the position number
     */
    public String getPositionNumber() {
        return positionNumber;
    }

    /**
     * Sets the position number.
     *
     * @param positionNumber the new position number
     */
    public void setPositionNumber(String positionNumber) {
        this.positionNumber = positionNumber;
    }

    /**
     * Gets the position level.
     *
     * @return the position level
     */
    public String getPositionLevel() {
        return positionLevel;
    }

    /**
     * Sets the position level.
     *
     * @param positionLevel the new position level
     */
    public void setPositionLevel(String positionLevel) {
        this.positionLevel = positionLevel;
    }

    /**
     * Gets the attachment id.
     *
     * @return the attachment id
     */
    public String getAttachmentId() {
        return attachmentId;
    }

    /**
     * Sets the attachment id.
     *
     * @param attachmentId the new attachment id
     */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getPanorama() {
        return panorama;
    }

    public void setPanorama(String panorama) {
        this.panorama = panorama;
    }

    public Date getInspectStartDate() {
        return inspectStartDate;
    }

    public void setInspectStartDate(Date inspectStartDate) {
        this.inspectStartDate = inspectStartDate;
    }

    public List<OrganizationPic> getPicList() {
        return picList;
    }

    public void setPicList(List<OrganizationPic> picList) {
        this.picList = picList;
    }

    public Boolean getChangeStation() {
        return changeStation;
    }

    public void setChangeStation(Boolean changeStation) {
        this.changeStation = changeStation;
    }

    public Boolean getStopStatus() {
        return stopStatus;
    }

    public void setStopStatus(Boolean stopStatus) {
        this.stopStatus = stopStatus;
    }

    public Date getStopStartDate() {
        return stopStartDate;
    }

    public void setStopStartDate(Date stopStartDate) {
        this.stopStartDate = stopStartDate;
    }

    public Date getStopEndDate() {
        return stopEndDate;
    }

    public void setStopEndDate(Date stopEndDate) {
        this.stopEndDate = stopEndDate;
    }

    public String getStopMemo() {
        return stopMemo;
    }

    public void setStopMemo(String stopMemo) {
        this.stopMemo = stopMemo;
    }

    public Integer getHasWhole() {
        return hasWhole;
    }

    public void setHasWhole(Integer hasWhole) {
        this.hasWhole = hasWhole;
    }

    public Integer getHasBIM() {
        return hasBIM;
    }

    public void setHasBIM(Integer hasBIM) {
        this.hasBIM = hasBIM;
    }

    public Integer getHasScene() {
        return hasScene;
    }

    public void setHasScene(Integer hasScene) {
        this.hasScene = hasScene;
    }

    public Integer getHasConduit() {
        return hasConduit;
    }

    public void setHasConduit(Integer hasConduit) {
        this.hasConduit = hasConduit;
    }

    public Integer getHasGeology() {
        return hasGeology;
    }

    public void setHasGeology(Integer hasGeology) {
        this.hasGeology = hasGeology;
    }
}
