package tests;

import com.codeborne.selenide.WebDriverRunner;
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
public class DemoWebShopHomeWork18Tests extends TestBase {

    String productId;

    @Test
    @DisplayName("Успешная авторизация в demowebshop (API + UI)")
    void loginWithApiAndCustomListenerTest() {
        step("Получение куки авторизации через обращение к эндпоинту", () -> {
            String authCookiesValue = given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .log().all()
                    .when()
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract().cookie(authCookieName);


            step("Открытие браузера с легковесным изображением для добавления куки авторизации", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
            Cookie authCookie = new Cookie(authCookieName, authCookiesValue);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);

        });

        step("Проверка авторизации в браузере", () -> {
            open(baseUrl);
            $(".account").shouldHave(text(login));
        });
    }

    @Test
    @DisplayName("Добавление товара в Compare products")
    void addProductToCompareList() {
        step("Получение id товара через запрос к его странице", () -> {
            productId = given()
                    .when()
                    .log().all()
                    .get("/141-inch-laptop")
                    .then()
                    .statusCode(200)
                    .extract().cookie(viewedCookie);
        });

        step("Открытие страницы с легковесным изображением и добавление куки товара к сравнению", () -> {
            open(baseUrl + "/Themes/DefaultClean/Content/images/logo.png");
            Cookie compareCookie = new Cookie(compareListCookieName, compareListCookie + productId);
            WebDriverRunner.getWebDriver().manage().addCookie(compareCookie);
            refresh();
        });

        step("Проверка товара", () -> {
            open(baseUrl + "/compareproducts");
            $(".product-name").shouldHave(text("14.1-inch Laptop"));
        });
    }

    @Test
    @DisplayName("Добавление товара в Shopping cart")
    void addProductToShoppingCart() {
        step("Получение id товара через запрос к его странице", () -> {
            productId = given()
                    .when()
                    .log().all()
                    .get("/computing-and-internet")
                    .then()
                    .statusCode(200)
                    .extract().cookie(viewedCookie);
        });

        step("Открытие страницы с легковесным изображением и добавление куки товара к сравнению", () -> {
            open(baseUrl + "/Themes/DefaultClean/Content/images/logo.png");
            Cookie compareCookie = new Cookie(compareListCookieName, compareListCookie + productId);
            WebDriverRunner.getWebDriver().manage().addCookie(compareCookie);
            refresh();
        });

        step("Проверка товара", () -> {
            open(baseUrl + "/cart");
            $(".product").shouldHave(text("Computing and Internet"));
        });
    }
}
