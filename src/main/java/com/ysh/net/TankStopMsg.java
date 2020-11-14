package com.ysh.net;

import com.ysh.Direction;
import com.ysh.Tank;
import com.ysh.TankFrame;

import java.io.*;
import java.util.UUID;

/**
 * @Author: Henry Yi
 * @Date: 7/18/2020 - 10:51
 * @Description: com.ysh.net
 * @Version: 1.0
 */
public class TankStopMsg extends Msg {
    private UUID id;
    private int x, y;

    public TankStopMsg() {
    }

    public TankStopMsg(UUID id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
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

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            id = new UUID(dis.readLong(), dis.readLong());
            x = dis.readInt();
            y = dis.readInt();
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

    @Override
    public void handle() {
        if (id.equals(TankFrame.INSTANCE.getGameModel().getMyTank().getId())) {
            return;
        }

        Tank t = TankFrame.INSTANCE.getGameModel().findTankByUUID(this.id);

        if (t == null) {
            return;
        }

        t.setMoving(false);
        t.setX(x);
        t.setY(y);
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankStop;
    }

    @Override
    public String toString() {
        return "TankStop{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
