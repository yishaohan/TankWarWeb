package com.ysh;

import java.awt.*;

/**
 * @Author: Henry Yi
 * @Date: 6/26/2020 - 19:31
 * @Description: com.ysh
 * @Version: 1.0
 */
public class Mine extends AbstractGameObject {
    private int x, y, width, height;
    private boolean live;

    public Mine() {
        this.width = 50;
        this.height = 50;
        this.live = true;
        this.x = (int)(Math.random()*(TankFrame.getWIDTH()-width-80))+40;
        this.y = (int)(Math.random()*(TankFrame.getHEIGHT()-height-80))+40;

        rect.setBounds(x, y, width, height);
    }

    public Mine(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        this.live = true;

        rect.setBounds(x, y, width, height);
    }

    public void randomLoc() {
        x = (int)(Math.random()*(TankFrame.getWIDTH()-width-80))+40;
        y = (int)(Math.random()*(TankFrame.getHEIGHT()-height-80))+40;

        rect.setLocation(x, y);
    }



    @Override
    public void collide(AbstractGameObject other) {
        if (other instanceof Player) {
            Player temp = (Player) other;
            if (CollideDetector.collide(other, this)) {
                temp.die();
                this.die();
            }
        } else if (other instanceof Tank) {
            Tank temp = (Tank) other;
            if (CollideDetector.collide(other, this)) {
                temp.die();
                this.die();
            }
        } else if (other instanceof Bullet) {
            Bullet temp = (Bullet) other;
            if (CollideDetector.collide(other, this)) {
                temp.die();
                this.die();
            }
        }
    }

    Rectangle rect = new Rectangle(x, y, width, height);
    public Rectangle getRect() {
        return rect;
    }

    private void die() {
        live = false;
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Explode(x, y));
    }


    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x, y, width, height);
        g.setColor(c);
    }

    @Override
    public boolean isLive() {
        return live;
    }
}
