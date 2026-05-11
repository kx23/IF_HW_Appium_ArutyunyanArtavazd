package pages;

import driver.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BasePage<T extends BasePage<T>> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final AndroidDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        PageFactory.initElements(
                new AppiumFieldDecorator(driver, Duration.ofSeconds(10)),
                this
        );

        log.debug("PageObject инициализирован: {}", getClass().getSimpleName());
    }

    protected T self() {
        return (T) this;
    }
    protected void waitUntilVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
}