package com.ysh;

import com.ysh.net.TankJoinMsg;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

public class Tank extends AbstractGameObject {
    private int x, y;
    private int oldX, oldY;
    public int speed = 5;
    public Direction dir;
    private Group group;
    private int width = ResourceMgr.goodTankU.getWidth(), height = ResourceMgr.goodTankU.getHeight();
    private boolean live = true;
    private boolean moving;
    private UUID id;

    public Tank(Group group) {
        this.x = (int) (Math.random() * (TankFrame.getWIDTH() - width - 150)) + 50;
        this.y = (int) (Math.random() * (TankFrame.getHEIGHT() - height - 150)) + 50;
        this.oldX = x;
        this.oldY = y;
        this.dir = Direction.randomDirection();
        this.group = group;
        this.id = UUID.randomUUID();
    }

    public Tank(TankJoinMsg tjm) {
        this.x = tjm.getX();
        this.y = tjm.getY();
        this.dir = tjm.getDir();
        this.group = tjm.getGroup();
        this.id = tjm.getId();

        this.rect = new Rectangle(x, y);
        this.oldX = x;
        this.oldY = y;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public void randomLoc() {
        x = (int) (Math.random() * (TankFrame.getWIDTH() - width - 150)) + 50;
        y = (int) (Math.random() * (TankFrame.getHEIGHT() - height - 150)) + 50;
    }

    public boolean isLive() {
        return live;
    }

    public void paint(Graphics g) {
        if (!this.isLive()) {
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString(id.toString(), x-50, y-10 );
        g.setColor(c);
//    System.out.println(dir);
        if (this.group == Group.GOOD) {
            switch (dir) {
                case L:
                    g.drawImage(ResourceMgr.goodTankL, x, y, null);
                    break;
                case U:
                    g.drawImage(ResourceMgr.goodTankU, x, y, null);
                    break;
                case R:
                    g.drawImage(ResourceMgr.goodTankR, x, y, null);
                    break;
                case D:
                    g.drawImage(ResourceMgr.goodTankD, x, y, null);
                    break;
            }
        } else {
            switch (dir) {
                case L:
                    g.drawImage(ResourceMgr.badTankL, x, y, null);
                    break;
                case U:
                    g.drawImage(ResourceMgr.badTankU, x, y, null);
                    break;
                case R:
                    g.drawImage(ResourceMgr.badTankR, x, y, null);
                    break;
                case D:
                    g.drawImage(ResourceMgr.badTankD, x, y, null);
                    break;
            }
        }
        move();
    }

    public UUID getId() {
        return id;
    }

    public void collide(AbstractGameObject other) {
        if (other instanceof Tank) {
            Tank temp = (Tank) other;
            if (CollideDetector.collide(temp, this)) {
                this.retreat();
                temp.retreat();
            }
        }
    }

    Rectangle rect = new Rectangle();

    public Rectangle getRect() {
        rect.setBounds(x, y, width, height);
        return rect;
    }

    public Group getGroup() {
        return group;
    }

    private void move() {
        boundsCheck();
        if (!moving) {
            return;
        }
        oldX = x;
        oldY = y;
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
            default:
                throw new IllegalStateException("Unexpected value: " + dir);
        }
        /*if (r.nextInt(100) > 97) {
            fire();
        }*/
    }

    public void retreat() {
        x = oldX;
        y = oldY;
    }

    private Random r = new Random();

    private void RandomDir() {
        if (r.nextInt(100) > 90) {
            this.dir = Direction.randomDirection();
        }
    }


    private void boundsCheck() {
        if (x < 20 || x > (TankFrame.getWIDTH() - width - 20) || y < 40 || y > (TankFrame.getHEIGHT() - height - 20)) {
            retreat();
        }
    }


    private void fire() {
        int bX = x + width / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = y + height / 2 - ResourceMgr.bulletU.getHeight() / 2;
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Bullet(bX, bY, dir, group));
    }

    public void die() {
        this.setLive(false);
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Explode(x, y));
    }

    private void setLive(boolean b) {
        this.live = b;
    }

    private int count;
}


