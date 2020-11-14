package com.ysh;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: Henry Yi
 * @Date: 7/1/2020 - 14:22
 * @Description: com.ysh
 * @Version: 1.0
 */
public class GameModel implements Serializable {
    private Player myTank;
    private ArrayList<AbstractGameObject> objects;
    private Random r = new Random();

    public GameModel() {
        initGameObject();
    }

    private void initGameObject() {
        myTank = new Player(50 + r.nextInt(500), 50 + r.nextInt(500), Direction.randomDirection(), Group.values()[r.nextInt(2)]);
        objects = new ArrayList<AbstractGameObject>();
        int tankCount = Integer.parseInt(PropertyMgr.get("initTankCount"));
//        add(new Wall(400, 400));
//        add(new Wall(500, 500));
        add(myTank);
        for (int i = 0; i < tankCount; i++) {
            add(new Tank(Group.GOOD));
        }
        int teammateCount = Integer.parseInt(PropertyMgr.get("initTeammateCount"));
        for (int i = 0; i < teammateCount; i++) {
            add(new Tank(Group.BAD));
        }
        int mineCount = Integer.parseInt(PropertyMgr.get("initMineCount"));
        for (int i = 0; i < mineCount; i++) {
            add(new Mine());
        }

    }

    public void addWithoutDetection(AbstractGameObject go) {
        objects.add(go);
    }

    public void add(AbstractGameObject go) {
        for (int i = 0; i < objects.size(); i++) {
            if (CollideDetector.collide(go, objects.get(i))) {
                go.randomLoc();
                i = 0;
                continue;
            }
        }
        objects.add(go);
    }

    public void paint(Graphics g) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).isLive()) {
                objects.get(i).paint(g);
                for (int j = 0; j < objects.size(); j++) {
                    if (objects.get(j) == objects.get(i)) {
                        continue;
                    }
                    if (objects.get(j).isLive()) {
                        objects.get(i).collide(objects.get(j));
                    } else {
                        continue;
                    }
                }
            } else {
                if (objects.get(i) instanceof Player) {
                    myTank = null;
                }
                objects.remove(i);
                i--;
            }
        }
    }

    public Player getMyTank() {
        return myTank;
    }

    public Tank findTankByUUID(UUID id) {
        for (AbstractGameObject o : objects) {
            if (o instanceof Tank) {
                Tank t = (Tank) o;
                if (id.equals(t.getId())) {
                    return t;
                }
            }
        }
        return null;
    }
}
