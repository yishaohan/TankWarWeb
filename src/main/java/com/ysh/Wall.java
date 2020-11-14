package com.ysh;

import java.awt.*;

/**
 * @Author: Henry Yi
 * @Date: 6/26/2020 - 14:17
 * @Description: com.ysh
 * @Version: 1.0
 */
public class Wall extends AbstractGameObject {
    private int x, y;
    private int width, height;

    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;

        rect.setBounds(x, y, width, height);
    }



    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.lightGray);
        g.fillRect(x, y, width, height);
        g.setColor(c);
    }

    Rectangle rect = new Rectangle(x, y, width, height);
    public Rectangle getRect() {
        return rect;
    }

    public boolean isLive() {
        return true;
    }

    public void randomLoc() {

    }

    public void collide(AbstractGameObject other) {
        if (other instanceof Player) {
            Player temp = (Player)other;
            if(CollideDetector.collide(temp, this)) {
                temp.retreat();
            }
        } else if (other instanceof Tank) {
            Tank temp = (Tank)other;
            if(CollideDetector.collide(temp, this)) {
                temp.retreat();
            }
        } else if (other instanceof Bullet) {
            Bullet temp = (Bullet)other;
            if(CollideDetector.collide(temp, this)) {
                temp.die();
            }
        }
    }
}
