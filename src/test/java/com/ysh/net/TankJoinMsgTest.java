package com.ysh.net;

import com.ysh.Direction;
import com.ysh.Group;
import com.ysh.Player;
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
class TankJoinMsgTest {
    @Test
    void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new MsgEncoder());

        Player p = new Player(50, 100, Direction.R, Group.BAD);
        TankJoinMsg tjm = new TankJoinMsg(p);

        ch.writeOutbound(tjm);

        ByteBuf buf = ch.readOutbound();

        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        Direction dir = Direction.values()[buf.readInt()];
        Group group = Group.values()[buf.readInt()];
        boolean bSTOP = buf.readBoolean();
        UUID id = new UUID(buf.readLong(), buf.readLong());

        assertEquals(MsgType.TankJoin, msgType);
        assertEquals(33, length);
        assertEquals(50, x);
        assertEquals(100, y);
        assertEquals(Direction.R, dir);
        assertEquals(Group.BAD, group);
        assertFalse(bSTOP);
        assertEquals(p.getId(), id);
    }

    @Test
    void decode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        UUID id = UUID.randomUUID();

        ch.pipeline().addLast(new MsgDecoder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.TankJoin.ordinal());
        buf.writeInt(33);
        buf.writeInt(5);
        buf.writeInt(8);
        buf.writeInt(Direction.D.ordinal());
        buf.writeInt(Group.GOOD.ordinal());
        buf.writeBoolean(true);
        buf.writeLong(id.getMostSignificantBits());
        buf.writeLong(id.getLeastSignificantBits());
        ch.writeInbound(buf);

        TankJoinMsg tjm = ch.readInbound();
        assertEquals(MsgType.TankJoin, tjm.getMsgType());
        assertEquals(33, tjm.toBytes().length);
        assertEquals(5, tjm.getX());
        assertEquals(8, tjm.getY());
        assertEquals(Direction.D, tjm.getDir());
        assertEquals(Group.GOOD, tjm.getGroup());
        assertTrue(tjm.isbSTOP());
        assertEquals(id, tjm.getId());
    }
}