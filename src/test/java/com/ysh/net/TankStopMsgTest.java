package com.ysh.net;

import com.ysh.Direction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author: Henry Yi
 * @Date: 7/17/2020 - 11:37
 * @Description: com
 * @Version: 1.0
 */
class TankStopMsgTest {
    @Test
    void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new MsgEncoder());

        TankStopMsg tsm = new TankStopMsg(UUID.randomUUID(), 50, 100);

        ch.writeOutbound(tsm);

        ByteBuf buf = ch.readOutbound();

        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        UUID id = new UUID(buf.readLong(), buf.readLong());
        int x = buf.readInt();
        int y = buf.readInt();

        assertEquals(MsgType.TankStop, msgType);
        assertEquals(24, length);
        assertEquals(50, x);
        assertEquals(100, y);
    }

    @Test
    void decode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        UUID id = UUID.randomUUID();

        ch.pipeline().addLast(new MsgDecoder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.TankStop.ordinal());
        buf.writeInt(24);
        buf.writeLong(id.getMostSignificantBits());
        buf.writeLong(id.getLeastSignificantBits());
        buf.writeInt(5);
        buf.writeInt(8);
        ch.writeInbound(buf);

        TankStopMsg tsmm = ch.readInbound();
        assertEquals(MsgType.TankStop, tsmm.getMsgType());
        assertEquals(24, tsmm.toBytes().length);
        assertEquals(5, tsmm.getX());
        assertEquals(8, tsmm.getY());
        assertEquals(id, tsmm.getId());
    }
}