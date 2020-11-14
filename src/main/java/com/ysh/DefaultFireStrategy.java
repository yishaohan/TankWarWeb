package com.ysh;

import com.ysh.net.BulletNewMsg;
import com.ysh.net.Client;

/**
 * @Author: Henry Yi
 * @Date: 6/25/2020 - 21:29
 * @Description: com.ysh
 * @Version: 1.0
 */
public class DefaultFireStrategy implements FireStrategy {
    private static DefaultFireStrategy INSTANCE = new DefaultFireStrategy();
    private DefaultFireStrategy() {

    }
    public static DefaultFireStrategy getINSTANCE() {
        return INSTANCE;
    }
    @Override
    public void fire(Player p) {
        int bX = p.getX() + p.getWidth() / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = p.getY() + p.getHeight() / 2 - ResourceMgr.bulletU.getHeight() / 2;
        TankFrame.INSTANCE.getGameModel().addWithoutDetection(new Bullet(bX, bY, p.getDir(), p.getGroup()));
        Client.INSTANCE.send(new BulletNewMsg(bX, bY, p.getDir(), p.getGroup(), p.getId()));
    }
}