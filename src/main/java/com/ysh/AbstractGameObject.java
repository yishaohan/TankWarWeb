package com.ysh;

import java.awt.*;
import java.io.Serializable;

/**
 * @Author: Henry Yi
 * @Date: 6/26/2020 - 14:41
 * @Description: com.ysh
 * @Version: 1.0
 */
public abstract class AbstractGameObject implements Serializable {

    public abstract void randomLoc();

    public abstract void collide(AbstractGameObject other);

    public abstract Rectangle getRect();

    public abstract void paint(Graphics g);

    public abstract boolean isLive();
}
