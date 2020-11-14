package com.ysh;

import java.awt.*;

/**
 * @Author: Henry Yi
 * @Date: 6/11/2020 - 14:19
 * @Description: com.ysh
 * @Version: 1.0
 */
public class Explode extends AbstractGameObject {
    private int x, y;
    private int width, height;
    private int Max;
    private int step = 0;
    private boolean live = true;

    public Explode(int x, int y, int Max) {
        this.x = x;
        this.y = y;
        this.Max = Max;
        this.width = ResourceMgr.explodes[0].getWidth();
        this.height = ResourceMgr.explodes[0].getHeight();

        new Thread(() -> new Audio("audio/explode.wav").play()).start();
    }

    public void randomLoc() {

    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        this.Max = ResourceMgr.explodes.length;
        this.width = ResourceMgr.explodes[0].getWidth();
        this.height = ResourceMgr.explodes[0].getHeight();

        new Thread(() -> new Audio("audio/explode.wav").play()).start();
    }

    public void collide(AbstractGameObject other) {

    }

    public boolean isLive() {
        return live;
    }

    Rectangle rect = new Rectangle(x, y, width, height);
    public Rectangle getRect() {
        return rect;
    }

    public void paint(Graphics g) {
        if (!isLive()) {
            return;
        }
        if (step >= Max) {
            over();
            return;
        }
        Color c = g.getColor();
        g.drawImage(ResourceMgr.explodes[step++], x, y, null);
        g.setColor(c);
    }

    public void over() {
        live = false;
    }
}
