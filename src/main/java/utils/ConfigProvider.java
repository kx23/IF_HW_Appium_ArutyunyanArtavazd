package utils;

import org.aeonbits.owner.ConfigFactory;

public class ConfigProvider {

    private static final AppConfig config = ConfigFactory.create(AppConfig.class);

    private ConfigProvider() {}

    public static AppConfig getConfig() {
        return config;
    }
}