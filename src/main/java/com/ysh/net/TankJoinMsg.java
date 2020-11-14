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
public class TankJoinMsg extends Msg {
    private int x, y;
    private Direction dir;
    private boolean bSTOP;
    private Group group;

    private UUID id;

    public TankJoinMsg() {
    }

    public TankJoinMsg(Player p) {
        this.x = p.getX();
        this.y = p.getY();
        this.dir = p.dir;
        this.group = p.group;
        this.bSTOP = p.isbSTOP();
        this.id = p.getId();
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

    public boolean isbSTOP() {
        return bSTOP;
    }

    public void setbSTOP(boolean bSTOP) {
        this.bSTOP = bSTOP;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TankJoinMessage{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", bSTOP=" + bSTOP +
                ", group=" + group +
                ", id=" + id +
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
            dos.writeBoolean(bSTOP);
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
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
            bSTOP = dis.readBoolean();
            id = new UUID(dis.readLong(), dis.readLong());
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
        if (id.equals(TankFrame.INSTANCE.getGameModel().getMyTank().getId())) {
            return;
        }

        if (TankFrame.INSTANCE.getGameModel().findTankByUUID(this.id) != null) {
            return;
        }

        Tank t = new Tank(this);

        TankFrame.INSTANCE.getGameModel().addWithoutDetection(t);

        Client.INSTANCE.send(new TankJoinMsg(TankFrame.INSTANCE.getGameModel().getMyTank()));
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankJoin;
    }
}