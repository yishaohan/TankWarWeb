package com.ysh;

import java.awt.*;

/**
 * @Author: Henry Yi
 * @Date: 6/26/2020 - 18:00
 * @Description: com.ysh
 * @Version: 1.0
 */
public class CollideDetector {
    private static Rectangle rect = new Rectangle(), rectTank = new Rectangle();
    public static boolean collide(AbstractGameObject go, AbstractGameObject other) {
        rect = go.getRect();
        rectTank = other.getRect();
        if (rect.intersects(rectTank)) {
            return true;
        }
        return false;
    }
}
