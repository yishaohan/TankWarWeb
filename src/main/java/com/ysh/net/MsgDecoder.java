package com.ysh.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: Henry Yi
 * @Date: 7/17/2020 - 12:06
 * @Description: com.ysh.net
 * @Version: 1.0
 */
public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8) {
            return;
        }

        in.markReaderIndex();

        MsgType msgType = MsgType.values()[in.readInt()];

        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        Msg msg = null;
        msg = (Msg) Class.forName("com.ysh.net." + msgType.toString() + "Msg")
                .getDeclaredConstructor()
                .newInstance();

        msg.parse(bytes);
        out.add(msg);
    }
}
