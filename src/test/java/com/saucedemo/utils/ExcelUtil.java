package com.saucedemo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    private Workbook workbook;
    private Sheet sheet;

    // Constructor to load Excel file and sheet
    public ExcelUtil(String filePath, String sheetName) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
    }

    // Get row count
    public int getRowCount() {
        return sheet.getLastRowNum();
    }

    // Get cell data
    public String getCellData(int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(colIndex);
        if (cell == null) return ""; // Handle empty cells
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    // Get all rows as a List of arrays (each array is one row)
    public List<String[]> getAllRows() {
        List<String[]> data = new ArrayList<>();
        for (int i = 1; i <= getRowCount(); i++) { // Start from 1 to skip header
            String username = getCellData(i, 0);
            String password = getCellData(i, 1);
            data.add(new String[]{username, password});
        }
        return data;
    }

    // Close workbook
    public void close() throws IOException {
        workbook.close();
    }
}
