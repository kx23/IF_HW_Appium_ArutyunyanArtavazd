package utils;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface AppConfig extends Config {

    @Key("appium.server.url")
    String appiumServerUrl();

    @Key("android.device.name")
    String deviceName();

    @Key("android.platform.name")
    String platformName();

    @Key("android.platform.version")
    String platformVersion();

    @Key("android.automation.name")
    String automationName();

    @Key("android.app.name")
    String appName();

    @Key("android.app.package")
    String appPackage();

    @Key("android.app.activity")
    String appActivity();

    @Key("driver.implicit.wait")
    int implicitWait();

    @Key("driver.new.command.timeout")
    int newCommandTimeout();
}