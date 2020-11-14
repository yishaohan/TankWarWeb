package com.ysh;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author: Henry Yi
 * @Date: 6/23/2020 - 18:05
 * @Description: com.ysh
 * @Version: 1.0
 */
public class PropertyMgr {
    private static Properties props;

    static {
        props = new Properties();
        try {
            props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config.Properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
