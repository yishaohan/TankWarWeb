package com.ysh.net;

import com.ysh.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * @Author: Henry Yi
 * @Date: 7/9/2020 - 20:44
 * @Description: com.ysh.net.netty
 * @Version: 1.0
 */
public class Client {
    public static final Client INSTANCE = new Client();
    SocketChannel sc;
    String hostname;

    private Client() {

    }

    public static void main(String[] args) {
        new Client().launch();
    }

    public void launch() {

        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        hostname = System.getProperty("hostname");

        if (hostname == null) {
            hostname = "192.168.0.130";
        }
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                sc = ch;
                ch.pipeline()
                        .addLast(new MsgEncoder())
                        .addLast(new MsgDecoder())
                        .addLast(new MyHandler());
            }
        });
        try {
            ChannelFuture future = b.connect(hostname, 8888).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void send(Msg msg) {
//System.out.println(msg);
        if (sc != null) {
            sc.writeAndFlush(msg);
        }

    }

    class MyHandler extends SimpleChannelInboundHandler<Msg> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
//System.out.println(msg);
            msg.handle();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getGameModel().getMyTank()));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
