package com.ysh.net;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @Author: Henry Yi
 * @Date: 6/5/2020
 * @Description: PACKAGE_NAME
 * @Version: 1.0
 */
public class ImageTest {
    @Test
    public void test() {
        try {
            BufferedImage image = ImageIO.read(new File("C:\\Users\\yisha\\IdeaProjects\\Tank War\\src\\images\\bulletL.gif"));
            assertNotNull(image);

            System.out.println(ImageTest.class.getClassLoader().getResourceAsStream("com\\ysh\\main.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
        public void test2() {
        try {
            BufferedImage image = ImageIO.read(new File("C:\\Users\\yisha\\IdeaProjects\\Tank War\\src\\images\\bulletL.gif"));
            assertNotNull(image);

            System.out.println(ImageTest.class.getClassLoader().getResourceAsStream("com\\ysh\\main.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
