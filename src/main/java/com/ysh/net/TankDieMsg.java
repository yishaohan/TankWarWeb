package com.ysh.net;

import com.ysh.Tank;
import com.ysh.TankFrame;

import java.io.*;
import java.util.UUID;

/**
 * @Author: Henry Yi
 * @Date: 7/19/2020 - 17:49
 * @Description: com.ysh.net
 * @Version: 1.0
 */
public class TankDieMsg extends Msg {
    private UUID id;

    public TankDieMsg() {
    }

    public TankDieMsg(UUID id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "TankDieMsg{" +
                "id=" + id +
                '}';
    }

    public byte[] toBytes() {

        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
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

        Tank t = TankFrame.INSTANCE.getGameModel().findTankByUUID(id);

        if ( t == null) {
            return;
        }

        t.die();

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankDie;
    }
}
