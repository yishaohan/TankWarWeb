package com.ysh;

import com.ysh.net.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Player extends AbstractGameObject {
    private int x, y;
    private int oldX, oldY;
    public int speed = 5;
    public Direction dir;
    public Group group;
    private int width = ResourceMgr.goodTankU.getWidth(), height = ResourceMgr.goodTankU.getHeight();
    private boolean live = true;
    private boolean bL, bU, bR, bD, bSTOP;
    private int mineCount = 0;
    private FireStrategy fs;
    private UUID id = UUID.randomUUID();

    public Player(int x, int y, Direction dir, Group group) {
        this.x = x;
        this.y = y;
        oldX = x;
        oldY = y;
        this.dir = dir;
        this.group = group;

        initFireStrategy();
    }

    public void initFireStrategy() {
        String fireStrategyName = PropertyMgr.get("FS");
        try {
            fs = (FireStrategy) Class.forName(fireStrategyName).getMethod("getINSTANCE").invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isLive() {
        return live;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isbSTOP() {
        return bSTOP;
    }

    public UUID getId() {
        return id;
    }

    public Direction getDir() {
        return dir;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void paint(Graphics g) {

        if (!this.isLive()) {
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.yellow);
        g.drawString(id.toString(), x-50, y-10 );
        g.setColor(c);

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
        } else if (this.group == Group.BAD) {
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

    public void randomLoc() {

    }

    public void KeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }

    }

    public Group getGroup() {
        return group;
    }

    private void setMainDir() {
        boolean oldbSTOP = bSTOP;
        Direction oldDir = dir;
        if (!bL && !bU && !bR && !bD) {
            bSTOP = true;
            if (!oldbSTOP) {
                Client.INSTANCE.send(new TankStopMsg(id, x, y));
            }
        } else {
            bSTOP = false;
            if (bL && !bU && !bR && !bD) {
                dir = Direction.L;
            }
            if (!bL && bU && !bR && !bD) {
                dir = Direction.U;
            }
            if (!bL && !bU && bR && !bD) {
                dir = Direction.R;
            }
            if (!bL && !bU && !bR && bD) {
                dir = Direction.D;
            }
            if (!oldDir.equals(dir)) {
                Client.INSTANCE.send(new TankMoveOrChangeDirMsg(id, x, y, dir));
            }
            if (oldbSTOP) {
                Client.INSTANCE.send(new TankMoveOrChangeDirMsg(id, x, y, dir));
            }
        }

    }

    public void retreat() {
        x = oldX;
        y = oldY;
    }

    Rectangle rect = new Rectangle();
    public Rectangle getRect() {
        rect.setBounds(x, y, width, height);
        return rect;
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

    private void move() {
        boundsCheck();
        setMainDir();
        if (bSTOP) {
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

    }

    private void boundsCheck() {
        if (x < 20 || x > (TankFrame.getWIDTH()-width-20) || y < 40 || y > (TankFrame.getHEIGHT()-height-20)) {
            retreat();
        }
    }


    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_SPACE:
                fire();
                break;
            case KeyEvent.VK_U:
//                if (mineCount++<=5) {
                    hideMine();
                    break;
//                }
        }

    }

    private void hideMine() {
        int xDif = 0;
        int yDif = 0;
        switch(dir) {
            case L:
                xDif+=60;
                break;
            case D:
                yDif-=60;
                break;
            case R:
                xDif-=60;
                break;
            case U:
                yDif+=60;
                break;
        }
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Mine(x+xDif, y+yDif));
        Client.INSTANCE.send(new MineNewMsg(x+xDif, y+yDif, id));
    }

    private void fire() {
        new Thread(() -> new Audio("audio/tank_fire.wav").play()).start();
        fs.fire(this);
    }

    public void die() {
        this.setLive(false);
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Explode(x, y));
        System.exit(0);
    }

    private void setLive(boolean b) {
        this.live = b;
    }
}