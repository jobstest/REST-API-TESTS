package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.demowebshop.ApiConfig;
import config.demowebshop.WebConfig;
import helpers.Attachments;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseTest {

    static String login;
    static String password;

    String cookieAuthName = "NOPCOMMERCE.AUTH";
    String compareListCookieName = "nop.CompareProducts";
    String viewedCookie = "NopCommerce.RecentlyViewedProducts";
    String compareListCookie = "CompareProductIds=";

    @BeforeAll
    static void config() {

        SelenideLogger.addListener("allure", new AllureSelenide());

        WebConfig webConfig = ConfigFactory.create(WebConfig.class, System.getProperties());

        login = webConfig.login();
        password = webConfig.password();

        ApiConfig apiConfig = ConfigFactory.create(ApiConfig.class, System.getProperties());

        RestAssured.baseURI = apiConfig.baseURI();
        Configuration.baseUrl = webConfig.basebUrl();

        String remote = apiConfig.server();
        String remoteUser = apiConfig.remoteUser();
        String remotePassword = apiConfig.remotePassword();
        Configuration.remote = "https://" + remoteUser + ":" + remotePassword + "@" + remote + "/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    void addAttachments() {
        Attachments.screenshotAs("Screenshot");
        Attachments.pageSource();
        Attachments.browserConsoleLogs();
        Attachments.addVideo();
        closeWebDriver();
    }
}
