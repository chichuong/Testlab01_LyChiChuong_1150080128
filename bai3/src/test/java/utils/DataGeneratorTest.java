package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class DataGeneratorTest {

    @Test
    public void generateExcelData() {
        String filePath = "src/test/resources/login_data.xlsx";
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet smokeSheet = workbook.createSheet("SmokeCases");
            createRow(smokeSheet, 0, "username", "password", "expected_url", "description");
            createRow(smokeSheet, 1, "student", "Password123", "https://practicetestautomation.com/logged-in-successfully/", "Login valid account");
            createRow(smokeSheet, 2, "student", "Password123", "https://practicetestautomation.com/logged-in-successfully/", "Login valid account 2");
            createRow(smokeSheet, 3, "student", "Password123", "https://practicetestautomation.com/logged-in-successfully/", "Login valid account 3");

            Sheet negativeSheet = workbook.createSheet("NegativeCases");
            createRow(negativeSheet, 0, "username", "password", "expected_error", "description");
            createRow(negativeSheet, 1, "incorrectUser", "Password123", "Your username is invalid!", "Invalid username");
            createRow(negativeSheet, 2, "student", "incorrectPassword", "Your password is invalid!", "Invalid password");
            createRow(negativeSheet, 3, "", "Password123", "Your username is invalid!", "Empty username");
            createRow(negativeSheet, 4, "student", "", "Your password is invalid!", "Empty password");
            createRow(negativeSheet, 5, "", "", "Your username is invalid!", "Empty everything");

            Sheet boundarySheet = workbook.createSheet("BoundaryCases");
            createRow(boundarySheet, 0, "username", "password", "expected_error", "description");
            createRow(boundarySheet, 1, "student2222222222222222222222222", "Password123", "Your username is invalid!", "Very long username");
            createRow(boundarySheet, 2, "st@ud$ent", "Password123", "Your username is invalid!", "Special characters in username");
            createRow(boundarySheet, 3, "' OR 1=1 --", "Password123", "Your username is invalid!", "SQL injection pattern username");
            createRow(boundarySheet, 4, "student", "' OR 1=1 --", "Your password is invalid!", "SQL injection pattern password");

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                System.out.println("Excel generated successfully at " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createRow(Sheet sheet, int rowNum, String... values) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(values[i]);
        }
    }
}
