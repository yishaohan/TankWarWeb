package com.ysh.net;

import com.ysh.*;

import java.io.*;
import java.util.UUID;

/**
 * @Author: Henry Yi
 * @Date: 7/17/2020 - 10:53
 * @Description: net
 * @Version: 1.0
 */
public class BulletNewMsg extends Msg {
    private int x, y;
    private Direction dir;
    private Group group;
    private UUID playerId;

    public BulletNewMsg() {
    }

    public BulletNewMsg(int x, int y, Direction dir, Group group, UUID id) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.playerId = id;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "BulletJoinMsg{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", group=" + group +
                '}';
    }

    public byte[] toBytes() {

        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeInt(group.ordinal());
            dos.writeLong(playerId.getMostSignificantBits());
            dos.writeLong(playerId.getLeastSignificantBits());
            dos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            x = dis.readInt();
            y = dis.readInt();
            dir = Direction.values()[dis.readInt()];
            group = Group.values()[dis.readInt()];
            playerId = new UUID(dis.readLong(), dis.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handle() {
        if (playerId.equals(TankFrame.INSTANCE.getGameModel().getMyTank().getId())) {
            return;
        }

        Bullet b = new Bullet(x, y, dir, group);

        TankFrame.INSTANCE.getGameModel().addWithoutDetection(b);

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.BulletNew;
    }
}