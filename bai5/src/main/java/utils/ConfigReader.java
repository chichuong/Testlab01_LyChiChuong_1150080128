package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    private Properties properties;

    private ConfigReader() {
        // Fallback to "dev" if not provided via maven
        String env = System.getProperty("env", "dev");
        String path = "src/test/resources/config-" + env + ".properties";
        try {
            FileInputStream input = new FileInputStream(path);
            properties = new Properties();
            properties.load(input);
            input.close();
            // Log yêu cầu của bài toán
            System.out.println("Đang dùng môi trường: " + env);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not read properties file: " + path);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
