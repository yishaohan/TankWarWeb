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
public class MineNewMsg extends Msg {
    private int x, y;
    private UUID playerId;

    public MineNewMsg() {
    }

    public MineNewMsg(int x, int y, UUID id) {
        this.x = x;
        this.y = y;
        playerId = id;
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


    public byte[] toBytes() {

        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeInt(x);
            dos.writeInt(y);
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

        Mine m = new Mine(x, y);

        TankFrame.INSTANCE.getGameModel().addWithoutDetection(m);

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.MineNew;
    }
}