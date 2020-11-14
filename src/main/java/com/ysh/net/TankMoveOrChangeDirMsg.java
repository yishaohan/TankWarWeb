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
public class TankMoveOrChangeDirMsg extends Msg {
    private UUID id;
    private int x, y;
    private Direction dir;

    public TankMoveOrChangeDirMsg() {
    }

    public TankMoveOrChangeDirMsg(UUID id, int x, int y, Direction dir) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
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

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
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
            dos.writeInt(dir.ordinal());
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
            dir = Direction.values()[dis.readInt()];
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

        t.setMoving(true);
        t.setX(x);
        t.setY(y);
        t.setDir(dir);
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankMoveOrChangeDir;
    }

    @Override
    public String toString() {
        return "TankStartMovingMsg{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                '}';
    }
}
