package com.ysh;

import com.ysh.net.BulletNewMsg;
import com.ysh.net.Client;

/**
 * @Author: Henry Yi
 * @Date: 6/25/2020 - 21:36
 * @Description: com.ysh
 * @Version: 1.0
 */
public class AllDirectionFireStrategy implements FireStrategy{
    private static AllDirectionFireStrategy INSTANCE = new AllDirectionFireStrategy();
    private AllDirectionFireStrategy() {

    }
    public static AllDirectionFireStrategy getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void fire(Player p) {
        int bX = p.getX() + p.getWidth() / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = p.getY() + p.getHeight() / 2 - ResourceMgr.bulletU.getHeight() / 2;
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Bullet(bX, bY, Direction.U, p.getGroup()));
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Bullet(bX, bY, Direction.D, p.getGroup()));
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Bullet(bX, bY, Direction.L, p.getGroup()));
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Bullet(bX, bY, Direction.R, p.getGroup()));
        Client.INSTANCE.send(new BulletNewMsg(bX, bY, Direction.U, p.getGroup(), p.getId()));
        Client.INSTANCE.send(new BulletNewMsg(bX, bY, Direction.D, p.getGroup(), p.getId()));
        Client.INSTANCE.send(new BulletNewMsg(bX, bY, Direction.L, p.getGroup(), p.getId()));
        Client.INSTANCE.send(new BulletNewMsg(bX, bY, Direction.R, p.getGroup(), p.getId()));
    }
}
