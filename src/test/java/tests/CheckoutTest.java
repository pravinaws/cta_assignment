package tests;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.Cart;
import pages.Checkout;
import pages.Login;
import utils.CrossBrowser;
import utils.ExtentReport;
import utils.Screenshots;
import utils.TestData;

public class CheckoutTest {
    WebDriver driver;
    Login login;
    Cart cart;
    Checkout checkout;
    Properties properties;

    @BeforeClass
    public void setup() throws IOException {
    	String browserName = properties.getProperty("browser"); // "chrome", "firefox", or "edge"
        driver = CrossBrowser.getBrowser(browserName);
    	ExtentReport.setupExtentReport();
        ExtentReport.test = ExtentReport.extent.createTest("Checkout Test");
    	/*
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Load properties
        properties = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        properties.load(fis);

        // Open URL
        driver.get(properties.getProperty("url"));

        // Initialize pages
        login = new Login(driver);
        cart = new Cart(driver);
        checkout = new Checkout(driver);
        */
    }

    @Test
    public void testCheckoutProcess() throws IOException {
    	try {
        String username = TestData.getData("Login", 1, 0);
        String password = TestData.getData("Login", 1, 1);
        String firstName = TestData.getData("Checkout", 1, 0);
        String lastName = TestData.getData("Checkout", 1, 1);
        String postalCode = TestData.getData("Checkout", 1, 2);

        login.login(username, password);
        cart.proceedToCheckout();
        checkout.fillDetails(firstName, lastName, postalCode);
    	} catch (Exception e) {
        Screenshots.captureScreenshot(driver, "CheckoutTest_Failure");
        throw e;
    }
    }

    @AfterClass
    public void teardown() {
        ExtentReport.flushReport();
        driver.quit();
    }
}
