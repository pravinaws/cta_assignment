package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Cart{
    WebDriver driver;

    @FindBy(xpath = "//button[@id='checkout']")
    WebElement checkoutButton;

    public Cart(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void proceedToCheckout() {
        checkoutButton.click();
    }
}
