package tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@Tag("api_test")
public class DemoWebShopHomeWork18Tests extends BaseTest {


    String authCookieValue;
    String productId;

    @Test
    public void loginTest(){

        step("Получение куки авторизации через обращение к эндпоинту", () -> {
            authCookieValue = given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .log().uri()
                    .log().params()
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(302)
                    .extract().cookie(cookieAuthName);
        });

        step("Открытие браузера с легковесным изображением для добавления куки авторизации", () -> {
            Selenide.open("/Themes/DefaultClean/Content/images/logo.png");
            Cookie authCookie = new Cookie(cookieAuthName, authCookieValue);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        });

        step("Проверка авторизации в браузере", () -> {
            open(baseUrl);
            $(".account").shouldHave(text(login));
        });
    }

    @Test
    public void addProductsToCompareList(){

        step("Получение id товара через запрос к эндпоинту", () -> {
            productId = given()
                    .when()
                    .log().all()
                    .get("/build-your-cheap-own-computer")
                    .then()
                    .statusCode(200)
                    .extract().cookie(viewedCookie).substring(25);
        });

        step("Открытие страницы с легковесным изображением и добавление куки товара к сравнению", () -> {
            open(baseUrl + "/Themes/DefaultClean/Content/images/logo.png");
            Cookie compareCookie = new Cookie(compareListCookieName, compareListCookie + productId);
            WebDriverRunner.getWebDriver().manage().addCookie(compareCookie);
            refresh();
        });

        step("Проверка списка товаров к сравнению", () -> {
            open(baseUrl + "/compareproducts");
            $(".product-name").shouldHave(text("Build your own cheap computer"));
        });
    }
}
