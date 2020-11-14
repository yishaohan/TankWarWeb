package com.ysh.code;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;


/**
 * @Author: Henry Yi
 * @Date: 7/9/2020 - 20:23
 * @Description: com.ysh.net.netty
 * @Version: 1.0
 */
public class ChatServer {

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void main(String[] args) {
        new ChatServer().launch();
    }

    public void launch() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
                .childHandler(new ChildInitializer());
        try {
            ChannelFuture future = b.bind(8888).sync();
            System.out.println("server started");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void add(Channel sc) {
        channels.add(sc);
    }

    class ChildInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline()
                    .addLast(new ChildHandler());
        }
    }

    class ChildHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            channels.writeAndFlush(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            channels.remove(ctx);
            ctx.close();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("A client connected");
            add(ctx.channel());
        }
    }
}