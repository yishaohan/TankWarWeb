package com.ysh.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: Henry Yi
 * @Date: 7/17/2020 - 11:19
 * @Description: net
 * @Version: 1.0
 */
public class MsgEncoder extends MessageToByteEncoder<Msg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getMsgType().ordinal());
        byte[] bytes = msg.toBytes();
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
