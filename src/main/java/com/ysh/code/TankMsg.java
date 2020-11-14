package com.ysh.code;

/**
 * @Author: Henry Yi
 * @Date: 7/14/2020 - 11:28
 * @Description: com.ysh.code
 * @Version: 1.0
 */
public class TankMsg {
    public int x, y;

    public TankMsg(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "TankMsg{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
