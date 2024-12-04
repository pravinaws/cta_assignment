package com.saucedemo.tests;

import com.aventstack.extentreports.Status;
import com.saucedemo.utils.ExcelUtil;
import com.saucedemo.utils.Screenshots;
import base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class SauceDemoTest extends BaseClass {
    private String baseUrl;
    private Actions actions;

    @BeforeMethod
    public void startTest() throws IOException {
        test = extent.createTest("SauceDemoTest", "Test to validate SauceDemo functionality");

        // Load the configuration file
        loadConfig();

        // Initialize Actions class for mouse movements and interactions
        actions = new Actions(driver);
    }

    @Test
    public void testSauceDemo() throws Exception {
        // Load user credentials from Excel
        String filePath = "src/test/resources/UserData.xlsx";
        ExcelUtil excelUtil = new ExcelUtil(filePath, "Sheet1");
        List<String[]> users = excelUtil.getAllRows();

        for (String[] user : users) {
            String username = user[0];
            String password = user[1];

            try {
                // Step 1: Open SauceDemo site
                driver.get(baseUrl);
                test.log(Status.INFO, "Navigated to SauceDemo site");

                // Step 2: Login
                WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
                usernameField.sendKeys(username);
                driver.findElement(By.id("password")).sendKeys(password);
                driver.findElement(By.id("login-button")).click();
                test.log(Status.INFO, "Logged in successfully with user: " + username);

                // Validate login success or handle login errors here
                if (driver.findElements(By.cssSelector(".error-message-container")).size() > 0) {
                    String errorMsg = driver.findElement(By.cssSelector(".error-message-container")).getText();
                    test.log(Status.WARNING, "Login failed for user: " + username + " - " + errorMsg);
                    continue; // Skip to the next user
                }

                // Step 3: Add first product to cart using Actions
                WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[contains(@class,'btn_inventory')])[1]")));
                actions.click(addToCartButton).perform();
                test.log(Status.PASS, "Product added to cart for user: " + username);

                // Step 4: Open cart and proceed to checkout
                WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
                actions.click(cartIcon).perform();

                WebElement checkoutButton = driver.findElement(By.id("checkout"));
                actions.click(checkoutButton).perform();

                // Fill out user details
                driver.findElement(By.id("first-name")).sendKeys("John");
                driver.findElement(By.id("last-name")).sendKeys("Doe");
                driver.findElement(By.id("postal-code")).sendKeys("12345");
                driver.findElement(By.id("continue")).click();

                // Step 5: Capture Payment Information, Shipping Information, Price Total, and Total before confirmation
                captureCheckoutDetails();

                // Step 6: Finish Checkout
                WebElement finishButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("finish")));
                actions.click(finishButton).perform();
                test.log(Status.PASS, "Checkout completed successfully for user: " + username);

                // Step 7: Validate confirmation
                WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".complete-header")));
                Assert.assertEquals(confirmationMessage.getText().toLowerCase(), "thank you for your order!".toLowerCase());
                test.log(Status.PASS, "Order confirmed for user: " + username);

            } catch (Exception e) {
                // Log error and capture screenshot
                String screenshotPath = Screenshots.captureScreenshot(driver, "Failure_" + username);
                test.log(Status.FAIL, "Test failed for user: " + username + " - " + e.getMessage());
                test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot for " + username);
            }
        }

        // Close Excel after the test
        excelUtil.close();
    }

    @AfterMethod
    public void endTest() {
        extent.flush(); // Write everything to the report
    }

    // Method to load configuration from the config file (baseUrl)
    private void loadConfig() throws IOException {
        Properties properties = new Properties();
        FileInputStream configFile = new FileInputStream("src/test/resources/config.properties");
        properties.load(configFile);

        // Fetch baseUrl from the config file
        baseUrl = properties.getProperty("baseUrl");
    }

    // Method to capture payment info, shipping info, price total, and total on the confirmation page
    private void captureCheckoutDetails() {
        try {
            // Wait for and capture Shipping Information
            WebElement shippingInfo = wait.withTimeout(Duration.ofSeconds(20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'summary_info')]//div[contains(text(),'Pony Express')]")));
            String shippingDetails = shippingInfo.getText();
            test.log(Status.INFO, "Shipping Information: " + shippingDetails);
            System.out.println("Shipping Info: " + shippingDetails);

            // Wait for and capture Payment Information
            WebElement paymentInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'summary_info')]//div[contains(text(),'SauceCard')]")));
            String paymentDetails = paymentInfo.getText();
            test.log(Status.INFO, "Payment Information: " + paymentDetails);
            System.out.println("Payment Info: " + paymentDetails);

            // Wait for and capture Price Total
            WebElement priceTotal = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='summary_subtotal_label' and contains(text(),'$')]")));
            String priceTotalText = priceTotal.getText();
            test.log(Status.INFO, "Price Total: " + priceTotalText);
            System.out.println("Price Total: " + priceTotalText);

            // Wait for and capture Total
            WebElement total = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='summary_total_label' and contains(text(),'$')]")));
            String totalText = total.getText();
            test.log(Status.INFO, "Total: " + totalText);
            System.out.println("Total: " + totalText);
        } catch (Exception e) {
            test.log(Status.FAIL, "Error capturing checkout details: " + e.getMessage());
        }
    }
}
