package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            // Đọc dữ liệu từ file properties theo yêu cầu "Tất cả dữ liệu test phải đọc từ file config"
            String path = "src/test/resources/config.properties";
            FileInputStream input = new FileInputStream(path);
            properties = new Properties();
            properties.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể tìm thấy file cấu hình!");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
