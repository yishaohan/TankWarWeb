package com.ysh.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: Henry Yi
 * @Date: 7/14/2020 - 11:38
 * @Description: com.ysh.code
 * @Version: 1.0
 */
public class TankMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8) {
            return;
        }
        int x = in.readInt();
        int y = in.readInt();

        out.add(new TankMsg(x, y));
    }
}