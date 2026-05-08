package com.example.tests;

import pages.SearchPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Поиск в Wikipedia")
class SearchTest extends BaseTest {

    @Test
    @DisplayName("Smoke: главный экран загружается после запуска приложения")
    void smokeMainScreenLoaded() {
        log.info("Проверяем, что главный экран отображается");
        assertTrue(mainPage.isDisplayed(),
                "Главный экран Wikipedia должен быть отображён после запуска");
        log.info("✓ Smoke-тест пройден: главный экран отображается");
    }

    @Test
    @DisplayName("Поиск 'Java' — первый результат содержит слово Java")
    void searchJavaFirstResultContainsJava() {
        final String searchQuery = "Java";

        log.info("Шаг 1: Открываем строку поиска");
        SearchPage searchPage = mainPage.tapSearch();

        log.info("Шаг 2: Вводим запрос '{}'", searchQuery);
        searchPage.enterQuery(searchQuery);

        log.info("Шаг 3: Проверяем наличие результатов");
        assertTrue(searchPage.hasResults(),
                "После ввода '" + searchQuery + "' должен быть хотя бы один результат");

        log.info("Шаг 4: Получаем заголовок первого результата");
        String firstResultTitle = searchPage.getFirstResultTitle();

        log.info("Шаг 5: Проверяем, что заголовок содержит '{}'", searchQuery);
        assertTrue(
                firstResultTitle.toLowerCase().contains(searchQuery.toLowerCase()),
                String.format(
                        "Первый результат поиска '%s' должен содержать '%s', но получили: '%s'",
                        searchQuery, searchQuery, firstResultTitle
                )
        );

        log.info("✓ Тест пройден: первый результат '{}' содержит '{}'",
                firstResultTitle, searchQuery);
    }

    @Test
    @DisplayName("Поиск 'Appium' — результаты не пусты")
    void searchAppiumHasResults() {
        final String searchQuery = "Appium";

        SearchPage searchPage = mainPage.tapSearch();
        searchPage.enterQuery(searchQuery);

        int resultsCount = searchPage.getResultsCount();
        log.info("Количество результатов для '{}': {}", searchQuery, resultsCount);

        assertTrue(resultsCount > 0,
                "Поиск по '" + searchQuery + "' должен вернуть хотя бы один результат");
    }

    @Test
    @DisplayName("Поиск 'Java' — открытие первой статьи")
    void searchJavaOpenFirstArticle() {
        final String searchQuery = "Java";

        SearchPage searchPage = mainPage
                .tapSearch()
                .enterQuery(searchQuery);

        assertTrue(searchPage.hasResults(), "Должны быть результаты поиска");

        String expectedTitle = searchPage.getFirstResultTitle();
        log.info("Открываем статью: '{}'", expectedTitle);

        var articlePage = searchPage.openFirstResult();

        assertTrue(articlePage.isDisplayed(),
                "Страница статьи должна быть отображена");

        String articleTitle = articlePage.getTitle();
        assertTrue(
                articleTitle.toLowerCase().contains(searchQuery.toLowerCase()),
                "Заголовок открытой статьи '" + articleTitle +
                "' должен содержать '" + searchQuery + "'"
        );

        log.info("✓ Тест пройден: статья '{}' успешно открыта", articleTitle);
    }
}
