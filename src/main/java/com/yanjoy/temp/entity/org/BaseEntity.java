package com.yanjoy.temp.entity.org;

import org.apache.ibatis.annotations.Update;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


// TODO: Auto-generated Javadoc
/**
 * 基础实体.
 *
 * @author lhb 2014/9/11
 * @version 1.0
 */
public class BaseEntity implements Serializable {


	/** The id. */
    private String id;

    /** The name. */
    private String name;

    /** The code. */
    private String code;

    /** The create user id. */
    private String createUserId;

    /** The create user name. */
    private String createUserName;

    /** The create date. */
    private Date createDate;

    /** The edit user id. */
    private String editUserId;

    /** The edit user name. */
    private String editUserName;

    /** The edit date. */
    private Date editDate;

    /** The delete flag. */
    private Boolean deleteFlag;

    /** The memo. */
    private String memo;


    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the creates the user id.
     *
     * @return the creates the user id
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * Sets the creates the user id.
     *
     * @param createUserId the new creates the user id
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**
     * Gets the creates the date.
     *
     * @return the creates the date
     */
    public Date getCreateDate() {
        return this.createDate;
    }

    /**
     * Sets the creates the date.
     *
     * @param createDate the new creates the date
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the edits the user id.
     *
     * @return the edits the user id
     */
    public String getEditUserId() {
        return editUserId;
    }

    /**
     * Sets the edits the user id.
     *
     * @param editUserId the new edits the user id
     */
    public void setEditUserId(String editUserId) {
        this.editUserId = editUserId;
    }

    public String getEditUserName() {
        return editUserName;
    }

    public void setEditUserName(String editUserName) {
        this.editUserName = editUserName;
    }

    /**
     * Gets the edits the date.
     *
     * @return the edits the date
     */
    public Date getEditDate() {
        return editDate;
    }

    /**
     * Sets the edits the date.
     *
     * @param editDate the new edits the date
     */
    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    /**
     * Gets the memo.
     *
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Sets the memo.
     *
     * @param memo the new memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param code the new code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the delete flag.
     *
     * @return the delete flag
     */
    public Boolean getDeleteFlag() {
        if (deleteFlag == null) {
            deleteFlag = false;
        }
        return deleteFlag;
    }

    /**
     * Sets the delete flag.
     *
     * @param deleteFlag the new delete flag
     */
    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}
