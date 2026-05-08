package pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class ArticlePage extends BasePage<ArticlePage> {

    @AndroidFindBy(xpath = "//*[@resource-id='pcs-edit-section-title-description']/preceding-sibling::android.view.View[1]")
    private WebElement articleTitle;

    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement backButton;

    public boolean isDisplayed() {
        waitUntilVisible(articleTitle);
        return articleTitle.isDisplayed();
    }

    public String getTitle() {
        String title = articleTitle.getText();
        return title;
    }

    public SearchPage goBack() {
        backButton.click();
        return new SearchPage();
    }
}
