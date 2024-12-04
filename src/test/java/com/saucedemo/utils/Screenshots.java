package com.saucedemo.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screenshots {

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        // Create a timestamp for unique screenshot names
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";

        // Specify the path where the screenshot will be saved
        String filePath = System.getProperty("user.dir") + "/screenshots/" + fileName;

        // Take the screenshot and save it
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(filePath));
            System.out.println("Screenshot saved: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
        return filePath; // Return the file path for reporting
    }
}
