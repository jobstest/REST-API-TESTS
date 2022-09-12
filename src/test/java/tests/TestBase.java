package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.demowebshop.ApiConfig;
import config.demowebshop.WebConfig;
import helpers.AllureAttachments;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    static String login;
    static String password;
    String authCookieName = "NOPCOMMERCE.AUTH"; //cookie авторизации

    String viewedCookie = "NopCommerce.RecentlyViewedProducts"; //cookie товара с id

    String compareListCookieName = "nop.CompareProducts"; // cookie страницы Compare products

    String compareListCookie = "CompareProductIds="; // cookie товара на странице Compare products равному id


    @BeforeAll
    static void beforeAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        WebConfig webConfig = ConfigFactory.create(WebConfig.class, System.getProperties());

        login = webConfig.login();
        password = webConfig.password();

        ApiConfig apiConfig = ConfigFactory.create(ApiConfig.class, System.getProperties());

        RestAssured.baseURI = apiConfig.baseURI();
        Configuration.baseUrl = webConfig.baseUrl();

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
    public void afterEach() {
        AllureAttachments.screenshotAs("Screenshot");
        AllureAttachments.pageSource();
        AllureAttachments.browserConsoleLogs();
        AllureAttachments.addVideo();
        closeWebDriver();
    }
}
