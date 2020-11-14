import java.util.Properties;

/**
 * @Author: Henry Yi
 * @Date: 6/23/2020 - 17:54
 * @Description: PACKAGE_NAME
 * @Version: 1.0
 */
public class TestConfig {
    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(TestConfig.class.getClassLoader().getResourceAsStream("config.Properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = props.getProperty("initTankCount");
        System.out.println(str);
    }
}
