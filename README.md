# Appium Mobile Test Framework

Java 17 | Maven | JUnit 5 | Appium 8.6.0 | UiAutomator2

---

## 1. Стек и зависимости

| Зависимость | Версия | Назначение |
|---|---|---|
| `io.appium:java-client` | 8.6.0 | Appium Java Client, AndroidDriver |
| `org.seleniumhq.selenium:selenium-java` | 4.13.0 | WebDriver API, PageFactory |
| `org.junit.jupiter:junit-jupiter-api` | 5.10.2 | JUnit 5 аннотации и assertions |
| `org.junit.jupiter:junit-jupiter-engine` | 5.10.2 | JUnit 5 движок запуска |
| `org.junit.jupiter:junit-jupiter-params` | 5.10.2 | Параметризованные тесты |
| `org.aeonbits.owner:owner` | 1.0.12 | Типобезопасное чтение конфигурации |
| `org.slf4j:slf4j-api` | 2.0.13 | Логирование API |
| `ch.qos.logback:logback-classic` | 1.5.6 | Реализация логирования |

---

## 2. Структура проекта

```
src/
├── main/
│   ├── java/
│   │   ├── driver/
│   │   │   └── DriverManager.java       # Управление экземпляром AppiumDriver
│   │   ├── pages/
│   │   │   ├── BasePage.java            # Базовый Page Object (общие методы)
│   │   │   ├── OnboardingPage.java      # Экран онбординга при первом запуске
│   │   │   ├── MainPage.java            # Главный экран приложения
│   │   │   ├── SearchPage.java          # Экран поиска статей
│   │   │   └── ArticlePage.java         # Экран просмотра статьи
│   │   └── utils/
│   │       ├── AppConfig.java           # Интерфейс конфигурации (Owner)
│   │       └── ConfigProvider.java      # Провайдер/фабрика конфигурации
│   └── resources/
│       ├── config.properties            # Параметры подключения и capabilities
│       └── logback.xml                  # Настройки логирования
└── test/
    ├── java/
    │   ├── BaseTest.java                # Базовый тест: setUp / tearDown
    │   └── SearchTest.java             # Тесты поиска статей
    └── resources/
        └── wikipedia.apk               # APK-файл тестируемого приложения
```

---

## 3. Конфигурация

Все параметры хранятся в `src/main/resources/config.properties`.

Для доступа к конфигурации используется библиотека **Owner (aeonbits)**:
- `AppConfig` — интерфейс с типизированными методами, каждый метод маппится на ключ через `@Key`
- `ConfigProvider` — хранит статический singleton экземпляр `AppConfig`, созданный через `ConfigFactory`

Параметры конфигурации:

| Ключ | Описание | Пример |
|---|---|---|
| `appium.server.url` | URL запущенного Appium сервера | `http://127.0.0.1:4723` |
| `android.device.name` | Имя AVD эмулятора | `Pixel_5_API_30` |
| `android.platform.name` | Платформа | `Android` |
| `android.platform.version` | Версия Android | `11.0` |
| `android.automation.name` | Драйвер автоматизации | `UiAutomator2` |
| `android.app.name` | Имя APK-файла в ресурсах | `wikipedia.apk` |
| `android.app.package` | Package тестируемого приложения | `org.wikipedia` |
| `android.app.activity` | Стартовая Activity | `org.wikipedia.main.MainActivity` |
| `driver.implicit.wait` | Неявное ожидание (сек) | `10` |
| `driver.new.command.timeout` | Таймаут команды (сек) | `60` |

---

## 4. Создание эмулятора

1. Открыть Android Studio → **Device Manager** (правая панель или Tools → Device Manager)
2. Нажать **Create Virtual Device**
3. Выбрать **Pixel 5** → Next
4. Выбрать образ **API 30 (Android 11.0)** — скачать если нет → Next
5. Имя устройства: `Pixel_5_API_30` → Finish
6. Запустить эмулятор кнопкой ▶
7. Убедиться, что устройство видно:
```bash
adb devices
# emulator-5554   device
```

---

## 5. Запуск Appium Inspector

1. Скачать последний релиз: https://github.com/appium/appium-inspector/releases
2. Запустить Appium сервер в терминале:
```bash
appium --allow-cors
```
3. Открыть Appium Inspector, указать параметры подключения:

   | Поле | Значение |
      |---|---|
   | Remote Host | `127.0.0.1` |
   | Remote Port | `4723` |
   | Remote Path | *(оставить пустым)* |

4. Вставить Capabilities во вкладке JSON, указав абсолютный путь до APK:
```json
{
  "platformName": "Android",
  "appium:deviceName": "Pixel_5_API_30",
  "appium:automationName": "UiAutomator2",
  "appium:app": "C:\\путь\\до\\проекта\\src\\test\\resources\\wikipedia.apk",
  "appium:appPackage": "org.wikipedia",
  "appium:appActivity": "org.wikipedia.main.MainActivity",
  "appium:noReset": true
}
```
5. Нажать **Start Session** — откроется зеркало эмулятора
6. Кликнуть на любой элемент → справа отобразятся его атрибуты, в том числе `resource-id` для `@AndroidFindBy`

---

## 6. Запуск тестов

Перед запуском убедиться, что активны:
- Appium сервер: `appium --allow-cors`
- Android эмулятор (`adb devices` показывает устройство)
- APK файл находится в `src/test/resources/`
- В Capabilities внутри Appium Inspector указан абсолютный путь до APK

```bash
# Все тесты
mvn test

# Конкретный тест
mvn test -Dtest=SearchTest#searchJavaFirstResultContainsJava
```

---

## 7. Тесты

| Метод | Описание |
|---|---|
| `smokeMainScreenLoaded` | Главный экран отображается после запуска приложения |
| `searchJavaFirstResultContainsJava` | Вводит "Java" в поиск, проверяет что первый результат содержит "Java" |
| `searchAppiumHasResults` | Поиск "Appium" возвращает хотя бы один результат |
| `searchJavaOpenFirstArticle` | Открывает первую статью из поиска "Java", проверяет заголовок |