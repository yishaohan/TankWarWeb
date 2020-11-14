package com.ysh.code;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @Author: Henry Yi
 * @Date: 7/9/2020 - 20:44
 * @Description: com.ysh.net.netty
 * @Version: 1.0
 */
public class ChatClient extends Frame {
    TextArea ta = new TextArea();
    TextField tf = new TextField();
    SocketChannel sc;
    String hostname;

    public static void main(String[] args) {
        new ChatClient().launch();
    }

    public void launch() {
        setSize(800, 600);
        setTitle("ChatRoom");
        setLocation(200, 50);
        add(tf, BorderLayout.SOUTH);
        add(ta, BorderLayout.NORTH);
        pack();
        setVisible(true);


        tf.addActionListener(new TFListener());
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        hostname = System.getProperty("hostname");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                try {
                    if (sc != null) {
                        sc.writeAndFlush(Unpooled.copiedBuffer("__bye__!".getBytes())).sync();
                    }
                    if (workerGroup != null) {
                        workerGroup.shutdownGracefully();
                    }
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                System.exit(0);
            }
        });
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
                        .addLast(new TankMsgDecoder())
                        .addLast(new TankMsgEncoder())
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

    class MyHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            TankMsg tm = (TankMsg) msg;
            ta.append(tm.toString() + "\n");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(new TankMsg(5, 8));
            System.out.println("update");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

    class TFListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = tf.getText();
            ByteBuf buf = Unpooled.copiedBuffer(str.getBytes());
            sc.writeAndFlush(buf);
            tf.setText("");
        }
    }
}
