package utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

public class ExcelDataGenerator {
    public static void main(String[] args) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("TestData");
            
            // Tạo Header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("URL");
            headerRow.createCell(1).setCellValue("Username");
            headerRow.createCell(2).setCellValue("Password");
            
            // Tạo dữ liệu test
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("https://the-internet.herokuapp.com/login");
            dataRow.createCell(1).setCellValue("tomsmith");
            dataRow.createCell(2).setCellValue("SuperSecretPassword!");
            
            // Ghi ra file excel
            try (FileOutputStream out = new FileOutputStream("testdata.xlsx")) {
                workbook.write(out);
                System.out.println("Excel file testdata.xlsx created successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
