package com.ysh.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: Henry Yi
 * @Date: 7/14/2020 - 11:32
 * @Description: com.ysh.code
 * @Version: 1.0
 */
public class TankMsgEncoder extends MessageToByteEncoder<TankMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TankMsg msg, ByteBuf out) throws Exception {
        out.writeInt(msg.x).writeInt(msg.y);
    }
}
