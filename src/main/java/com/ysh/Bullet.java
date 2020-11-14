package com.ysh;

import com.ysh.net.BulletNewMsg;
import com.ysh.net.Client;

import java.awt.*;

/**
 * @Author: Henry Yi
 * @Date: 6/11/2020 - 14:19
 * @Description: com.ysh
 * @Version: 1.0
 */
public class Bullet extends AbstractGameObject {
    private int x, y;
    private int width, height;
    private int speed = 15;
    private Direction dir;
    private Group group;
    private boolean live = true;


    public Bullet(int x, int y, Direction dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;

        width = ResourceMgr.bulletU.getWidth();
        height = ResourceMgr.bulletU.getHeight();

    }

    public Bullet (BulletNewMsg bjm) {
        this.x = bjm.getX();
        this.y = bjm.getY();
        this.dir = bjm.getDir();
        this.group = bjm.getGroup();

        width = ResourceMgr.bulletU.getWidth();
        height = ResourceMgr.bulletU.getHeight();
    }




    public void paint(Graphics g) {
        switch (dir) {
            case L:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case U:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case R:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            case D:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
        }
        move();
    }

    private void move() {
        switch (dir) {
            case L:
                x -= speed;
                break;
            case U:
                y -= speed;
                break;
            case R:
                x += speed;
                break;
            case D:
                y += speed;
                break;
        }
        boundsCheck();
    }

    public void collide(AbstractGameObject other) {
        if (other instanceof Player) {
            Player temp = (Player) other;
            if (this.group == temp.getGroup()) {
                return;
            }
            if (CollideDetector.collide(temp, this)) {
                this.die();
                temp.die();
            }
        } else if (other instanceof Tank) {
            Tank temp = (Tank) other;
            if (this.group == temp.getGroup()) {
                return;
            }
            if (CollideDetector.collide(temp, this)) {
                this.die();
                temp.die();
            }
        } /*else if (other instanceof Bullet) {
            Bullet temp = (Bullet) other;
            if (CollideDetector.collide(temp, this)) {
                this.die();
                temp.die();

                TankFrame.INSTANCE.add(new Explode(x, y, 4));
            }
        }*/
    }

    public void randomLoc() {

    }

    Rectangle rect = new Rectangle();
    public Rectangle getRect() {
        rect.setBounds(x, y, width, height);
        return rect;
    }

    private void setLive(boolean b) {
        live = false;
    }

    public void die() {
        this.setLive(false);
    }

    public boolean isLive() {
        return live;
    }

    private void boundsCheck() {
        if (x < 0 || x > TankFrame.getWIDTH() || y < 30 || y > TankFrame.getHEIGHT()) {
            live = false;
        }
    }
}
