package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseClass {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static ExtentReports extent;
    protected static ExtentTest test;

    @BeforeMethod
    public void setUp() {
        // WebDriver setup
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ExtentReports setup
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Tester", "Harshala Koktare");
            extent.setSystemInfo("Environment", "QA");
        }
    }

    @AfterMethod
    public void tearDown() {
        // Quit WebDriver
        if (driver != null) {
            driver.quit();
        }
    }
}
