package com.ysh;

import com.ysh.net.Client;

public class main {
    public static void main(String[] args) {
        TankFrame f = TankFrame.INSTANCE;

        new Thread(() -> new Audio("audio/war1.wav").loop()).start();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                f.repaint();
            }
        }).start();

        Client.INSTANCE.launch();
    }
}