package com.example.tests;

import driver.DriverManager;
import pages.MainPage;
import pages.OnboardingPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected MainPage mainPage;

    @BeforeEach
    void setUp() {
        log.info("=== Запуск теста: {} ===", getClass().getSimpleName());
        DriverManager.createDriver();
        mainPage = new OnboardingPage().skipOnboardingIfPresent();
        log.info("Приложение готово к тестированию");
    }

    @AfterEach
    void tearDown() {
        log.info("=== Завершение теста: {} ===", getClass().getSimpleName());
        DriverManager.quitDriver();
    }
}
