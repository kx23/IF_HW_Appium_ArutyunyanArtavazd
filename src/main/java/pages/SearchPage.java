package pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPage extends BasePage<SearchPage> {

    @AndroidFindBy(id = "org.wikipedia:id/search_src_text")
    private WebElement searchInput;

    @AndroidFindBy(id = "org.wikipedia:id/search_results_list")
    private WebElement searchResultsList;

    @AndroidFindBy(id = "org.wikipedia:id/page_list_item_title")
    private List<WebElement> resultTitles;

    @AndroidFindBy(id = "org.wikipedia:id/page_list_item_description")
    private List<WebElement> resultDescriptions;

    @AndroidFindBy(id = "org.wikipedia:id/results_text")
    private WebElement noResultsMessage;

    public SearchPage enterQuery(String query) {
        log.info("Вводим поисковый запрос: '{}'", query);
        searchInput.clear();
        searchInput.sendKeys(query);
        return this;
    }
    public String getFirstResultTitle() {
        if (resultTitles.isEmpty()) {
            throw new AssertionError("Список результатов поиска пуст — элементы не найдены");
        }
        String title = resultTitles.get(0).getText();
        log.info("Первый результат поиска: '{}'", title);
        return title;
    }

    public String getFirstResultDescription() {
        if (resultDescriptions.isEmpty()) {
            return "";
        }
        return resultDescriptions.get(0).getText();
    }
    public int getResultsCount() {
        return resultTitles.size();
    }

    public boolean hasResults() {
        return !resultTitles.isEmpty();
    }

    public ArticlePage openFirstResult() {
        log.info("Открываем первый результат: '{}'", getFirstResultTitle());
        resultTitles.get(0).click();
        return new ArticlePage();
    }
}
