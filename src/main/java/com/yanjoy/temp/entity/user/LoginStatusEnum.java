package com.yanjoy.temp.entity.user;

public enum LoginStatusEnum {
    /**
     * 成功
     */
    SUCCESS(1, "成功"),
    /**
     * 未注册
     */
    NO_REGISTER(2, "未注册"),
    /**
     * 失败
     */
    FAILED(3, "失败");


    private Integer value;

    private String name;

    LoginStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }
}
