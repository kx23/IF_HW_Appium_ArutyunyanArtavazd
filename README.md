# Appium Mobile Test Framework

Java 17 | Maven | JUnit 5 | Appium 9 | UiAutomator2 | Page Object

---

## 1. Установка окружения

### 1.1 Java 17
```bash
# Windows — скачать с https://adoptium.net (Temurin JDK 17)
# Проверить после установки:
java -version   # должно быть: openjdk 17.x.x
```

### 1.2 Apache Maven
```bash
# Скачать с https://maven.apache.org/download.cgi
# Распаковать, добавить bin/ в PATH
mvn -version    # должно быть: Apache Maven 3.9.x
```

### 1.3 Node.js (нужен для Appium)
```bash
# Скачать LTS с https://nodejs.org
node -v         # v18+ или v20+
npm -v
```

### 1.4 Appium Server
```bash
# Установить глобально через npm
npm install -g appium

# Проверить
appium -v       # должно быть: 2.x.x

# Запустить сервер (оставить терминал открытым!)
appium
# Appium теперь слушает на http://127.0.0.1:4723
```

### 1.5 UiAutomator2 Driver
```bash
# Установить драйвер для Android
appium driver install uiautomator2

# Проверить установленные драйверы
appium driver list --installed
```

### 1.6 Android Studio
1. Скачать с https://developer.android.com/studio
2. Установить, запустить **SDK Manager** (Tools → SDK Manager)
3. Во вкладке **SDK Platforms** установить **Android 8.1 (API 27)**
4. Во вкладке **SDK Tools** установить:
   - Android SDK Build-Tools
   - Android Emulator
   - Android SDK Platform-Tools

### 1.7 Переменные окружения
```bash
# Windows (через System Properties → Environment Variables)
ANDROID_HOME = C:\Users\<твой_юзер>\AppData\Local\Android\Sdk
JAVA_HOME    = C:\Program Files\Eclipse Adoptium\jdk-17.x.x

# Добавить в PATH:
%ANDROID_HOME%\tools
%ANDROID_HOME%\platform-tools
```

### 1.8 Appium Inspector (опционально, для инспекции элементов)
- Скачать последний релиз: https://github.com/appium/appium-inspector/releases
- Установить .exe (Windows) / .dmg (Mac)

---

## 2. Создание эмулятора Pixel 2 API 27

1. Открыть Android Studio → **AVD Manager** (Tools → Device Manager)
2. Нажать **Create Virtual Device**
3. Выбрать **Pixel 2** → Next
4. Выбрать **API 27 (Android 8.1 Oreo)** → Next (скачать образ если нет)
5. Имя: `Pixel_2_API_27` → Finish
6. Запустить эмулятор (кнопка ▶)
7. Проверить, что устройство подключено:
   ```bash
   adb devices
   # Должно появиться: emulator-5554  device
   ```

---

## 3. Добавление APK в проект

Скачай тестовое приложение Wikipedia APK:
- https://github.com/wikimedia/apps-android-wikipedia/releases
- Рекомендуемая версия: **2.7.50406-r-2022-01-14**

Положи файл в:
```
src/test/resources/wikipedia.apk
```

> ⚠️ Убедись, что имя файла совпадает с `android.app.name` в `config.properties`

---

## 4. Запуск тестов

```bash
# 1. Убедись, что запущены:
#    - Appium Server (appium)
#    - Android эмулятор

# 2. Перейди в корень проекта
cd appium-mobile-framework

# 3. Запусти все тесты
mvn test

# 4. Запустить конкретный тест
mvn test -Dtest=SearchTest#searchJavaFirstResultContainsJava

# 5. Запустить только smoke-тест
mvn test -Dtest=SearchTest#smokeMainScreenLoaded
```

---

## 5. Структура проекта

```
appium-mobile-framework/
├── pom.xml
└── src/
    └── test/
        ├── java/
        │   └── com/example/
        │       ├── driver/
        │       │   └── DriverManager.java       ← создание AndroidDriver
        │       ├── pages/
        │       │   ├── BasePage.java             ← базовый PageObject
        │       │   ├── OnboardingPage.java        ← экран приветствия
        │       │   ├── MainPage.java             ← главный экран
        │       │   ├── SearchPage.java           ← экран поиска
        │       │   └── ArticlePage.java          ← страница статьи
        │       ├── tests/
        │       │   ├── BaseTest.java             ← @BeforeEach/@AfterEach
        │       │   └── SearchTest.java           ← тесты поиска
        │       └── utils/
        │           └── ConfigReader.java         ← чтение config.properties
        └── resources/
            ├── config.properties                 ← capabilities и таймауты
            ├── logback-test.xml                  ← настройки логирования
            └── wikipedia.apk                     ← APK (положить вручную!)
```

---

## 6. Тесты

| Тест | Описание |
|------|----------|
| `smokeMainScreenLoaded` | Проверяет, что главный экран отображается после запуска |
| `searchJavaFirstResultContainsJava` | Вводит "Java" в поиск, проверяет что первый результат содержит "Java" |
| `searchAppiumHasResults` | Проверяет, что поиск "Appium" возвращает результаты |
| `searchJavaOpenFirstArticle` | Открывает первую статью из поиска "Java" и проверяет её заголовок |

---

## 7. Диагностика проблем

### Appium не подключается
```bash
# Проверь, что сервер запущен
appium
# Убедись что URL в config.properties: appium.server.url=http://127.0.0.1:4723
```

### Эмулятор не найден
```bash
adb devices                    # список устройств
adb kill-server && adb start-server   # перезапустить ADB
```

### Элементы не находятся
- Открой **Appium Inspector**
- Подключись к запущенной сессии или создай новую
- Проверь актуальные resource-id элементов в твоей версии APK
- Обнови локаторы в соответствующем Page Object

### Несовпадение версий APK
- Проверь `android.app.package` и `android.app.activity` в `config.properties`
- Узнать activity: `adb shell dumpsys package org.wikipedia | grep -i activity`

---

## 8. Как обновить локаторы под свою версию APK

Разные версии Wikipedia могут иметь разные `resource-id`. 

Алгоритм:
1. Запусти Appium Inspector
2. Capabilities: такие же, как в `config.properties`
3. Найди нужный элемент через Inspector
4. Скопируй `resource-id` → обнови `@AndroidFindBy(id = "...")` в PageObject
