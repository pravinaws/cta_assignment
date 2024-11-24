package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class TestData {
    public static String getData(String sheetName, int row, int col) throws IOException {
        FileInputStream fis = new FileInputStream("src/test/resources/TestData.xlsx");
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        Row rowData = sheet.getRow(row);
        Cell cell = rowData.getCell(col);
        String data = cell.getStringCellValue();
        workbook.close();
        return data;
    }
}
