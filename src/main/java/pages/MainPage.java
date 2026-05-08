package pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Главный экран Wikipedia.
 * Содержит строку поиска и другие основные элементы.
 */
public class MainPage extends BasePage<MainPage> {

    @AndroidFindBy(id = "org.wikipedia:id/search_container")
    private WebElement searchContainer;

    @AndroidFindBy(accessibility = "Search Wikipedia")
    private WebElement searchToolbarButton;


    public boolean isDisplayed() {
        try {
            return searchContainer.isDisplayed();
        } catch (Exception e) {
            log.warn("Главный экран не найден: {}", e.getMessage());
            return false;
        }
    }

    public SearchPage tapSearch() {
        log.info("Переходим к экрану поиска");
        searchContainer.click();
        return new SearchPage();
    }
}
