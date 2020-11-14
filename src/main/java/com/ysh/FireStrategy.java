package com.ysh;

import java.io.Serializable;

/**
 * @Author: Henry Yi
 * @Date: 6/25/2020 - 21:28
 * @Description: com.ysh
 * @Version: 1.0
 */
public interface FireStrategy extends Serializable {
    void fire(Player p);
}
