package driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigProvider;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> driverHolder = new ThreadLocal<>();

    private DriverManager() {}
    public static void createDriver() {
        log.info("Инициализация AndroidDriver...");

        UiAutomator2Options options = buildOptions();
        URL serverUrl = buildServerUrl();

        AndroidDriver driver = new AndroidDriver(serverUrl, options);
        configureTimeouts(driver);

        driverHolder.set(driver);
        log.info("AndroidDriver успешно создан и сконфигурирован");
    }

    public static AndroidDriver getDriver() {
        AndroidDriver driver = driverHolder.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "Драйвер не инициализирован. Вызовите DriverManager.createDriver() в @BeforeEach");
        }
        return driver;
    }

    public static void quitDriver() {
        AndroidDriver driver = driverHolder.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("AndroidDriver успешно завершён");
            } catch (Exception e) {
                log.warn("Ошибка при завершении драйвера: {}", e.getMessage());
            } finally {
                driverHolder.remove();
            }
        }
    }

    private static UiAutomator2Options buildOptions() {
        String apkName = ConfigProvider.getConfig().appName();

        URL apkUrl = DriverManager.class.getClassLoader().getResource(apkName);

        if (apkUrl == null) {
            throw new IllegalStateException(
                    "APK-файл не найден в classpath: " + apkName +
                            "\nПоложи файл в src/test/resources/" + apkName);
        }

        File apkFile;
        try {
            apkFile = Paths.get(apkUrl.toURI()).toFile();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка преобразования пути к APK", e);
        }

        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(ConfigProvider.getConfig().deviceName())
                .setPlatformName(ConfigProvider.getConfig().platformName())
                .setPlatformVersion(ConfigProvider.getConfig().platformVersion())
                .setAutomationName(ConfigProvider.getConfig().automationName())
                .setApp(apkFile.getAbsolutePath())
                .setAppPackage(ConfigProvider.getConfig().appPackage())
                .setAppActivity(ConfigProvider.getConfig().appActivity())
                .setAutoGrantPermissions(true)
                .setNoReset(false)
                .setNewCommandTimeout(
                        Duration.ofSeconds(
                                ConfigProvider.getConfig().newCommandTimeout()));

        log.info("Capabilities: device={}, platform={} {}, apk={}",
                options.getDeviceName(),
                options.getPlatformName(),
                options.getPlatformVersion(),
                apkFile.getAbsolutePath());

        return options;
    }

    private static URL buildServerUrl() {
        String urlStr = ConfigProvider.getConfig().appiumServerUrl();
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Неверный URL Appium сервера: " + urlStr, e);
        }
    }

    private static void configureTimeouts(AndroidDriver driver) {
        int implicitWaitSec = ConfigProvider.getConfig().implicitWait();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSec));
        log.info("Implicit Wait установлен: {} сек", implicitWaitSec);
    }
}
