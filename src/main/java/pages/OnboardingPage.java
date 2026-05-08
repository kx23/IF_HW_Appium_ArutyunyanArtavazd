package pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class OnboardingPage extends BasePage<OnboardingPage> {

    @AndroidFindBy(id = "org.wikipedia:id/fragment_onboarding_skip_button")
    private WebElement skipButton;

    @AndroidFindBy(id = "org.wikipedia:id/fragment_onboarding_forward_button")
    private WebElement continueButton;

    @AndroidFindBy(id = "org.wikipedia:id/fragment_onboarding_done_button")
    private WebElement getStartedButton;

    // -------------------------------------------------------------------------

    public boolean isOnboardingDisplayed() {
        try {
            return skipButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public MainPage skipOnboarding() {
        log.info("Пропускаем onboarding");
        skipButton.click();
        return new MainPage();
    }

    public MainPage skipOnboardingIfPresent() {
        if (isOnboardingDisplayed()) {
            return skipOnboarding();
        }
        log.info("Onboarding не обнаружен, переходим к главному экрану");
        return new MainPage();
    }
}
