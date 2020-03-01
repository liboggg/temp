package com.yanjoy.temp.utils;

import java.util.UUID;

public class IDNext {

    /**
     * 32位uuid
     * @return uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
