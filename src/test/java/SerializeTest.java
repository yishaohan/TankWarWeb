import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * @Author: Henry Yi
 * @Date: 7/1/2020 - 17:32
 * @Description: PACKAGE_NAME
 * @Version: 1.0
 */
public class SerializeTest {
    @Test
    public void testSaveImage() {
        try {
            T t = new T();
            File f = new File("d:/s.dat");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(t);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testLoadImage() {
        try {
            File f = new File("d:/s.dat");
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            T t = (T)ois.readObject();
            Apple a = t.a;
            Assertions.assertEquals(40, a.weight);
            Assertions.assertEquals(4, t.m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class T implements Serializable {
    int m = 4;
    int n = 8;
    Apple a = new Apple(40);
}

class Apple implements Serializable{
    int weight;
    public Apple(int w) {
        weight = w;
    }
}

