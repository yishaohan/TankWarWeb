package com.ysh.net;

import com.ysh.Direction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Henry Yi
 * @Date: 7/17/2020 - 11:37
 * @Description: com
 * @Version: 1.0
 */
class TankMoveOrChangeDirMsgTest {
    @Test
    void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new MsgEncoder());

        TankMoveOrChangeDirMsg tjm = new TankMoveOrChangeDirMsg(UUID.randomUUID(), 50, 100, Direction.D);

        ch.writeOutbound(tjm);

        ByteBuf buf = ch.readOutbound();

        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        UUID id = new UUID(buf.readLong(), buf.readLong());
        int x = buf.readInt();
        int y = buf.readInt();
        Direction dir = Direction.values()[buf.readInt()];

        assertEquals(MsgType.TankMoveOrChangeDir, msgType);
        assertEquals(28, length);
        assertEquals(50, x);
        assertEquals(100, y);
        assertEquals(Direction.D, dir);
    }

    @Test
    void decode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        UUID id = UUID.randomUUID();

        ch.pipeline().addLast(new MsgDecoder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.TankMoveOrChangeDir.ordinal());
        buf.writeInt(28);
        buf.writeLong(id.getMostSignificantBits());
        buf.writeLong(id.getLeastSignificantBits());
        buf.writeInt(5);
        buf.writeInt(8);
        buf.writeInt(Direction.D.ordinal());
        ch.writeInbound(buf);

        TankMoveOrChangeDirMsg tsmm = ch.readInbound();
        assertEquals(MsgType.TankMoveOrChangeDir, tsmm.getMsgType());
        assertEquals(28, tsmm.toBytes().length);
        assertEquals(5, tsmm.getX());
        assertEquals(8, tsmm.getY());
        assertEquals(Direction.D, tsmm.getDir());
        assertEquals(id, tsmm.getId());
    }
}