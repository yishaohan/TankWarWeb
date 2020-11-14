package com.ysh;

import com.ysh.net.Client;
import com.ysh.net.TankDieMsg;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class TankFrame extends Frame {
    public static final TankFrame INSTANCE = new TankFrame();
    private GameModel gm;

    public static int width, height;
    Image offScreenImage;

    private TankFrame() {
        width = Integer.parseInt(PropertyMgr.get("gameWidth"));
        height = Integer.parseInt(PropertyMgr.get("gameHeight"));
        gm = new GameModel();
        setLayout(null);
        setTitle("tank war");
        setLocation(100, 20);
        setSize(width, height);
        setVisible(true);
        addKeyListener(new KeyMonitor());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Client.INSTANCE.send(new TankDieMsg(gm.getMyTank().getId()));
                setVisible(false);
                System.exit(0);
            }
        });
    }

    public static int getWIDTH() {
        return width;
    }

    public static int getHEIGHT() {
        return height;
    }

    public GameModel getGameModel() {
        return gm;
    }

    @Override
    public void paint(Graphics g) {
        gm.paint(g);
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(width, height);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, width, height);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (gm.getMyTank() != null) {
                gm.getMyTank().KeyPressed(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_S ) {
                save();
            } else if (key == KeyEvent.VK_L) {
                load();
            } else if (gm.getMyTank() != null) {
                gm.getMyTank().keyReleased(e);
            }
        }
    }

    private void load() {
        File f = new File("d:/tank.dat");
        try(FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            gm = (GameModel) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save() {
        File f = new File("d:/tank.dat");
        try(
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
