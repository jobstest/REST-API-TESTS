package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static helpers.AllureRestAssuredFilter.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Tag("demowebshop")
public class DemowebshopTests {

    static String login = "qaguru@qa.guru",
            password = "qaguru@qa.guru1",
            authCookieName = "NOPCOMMERCE.AUTH";

    @BeforeAll
    static void configure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        Configuration.baseUrl = "http://demowebshop.tricentis.com";
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
    }

    @AfterEach
    void afterEach() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Successful authorization to some demowebshop (UI)")
    void loginTest() {
        step("Open login page", () ->
                open("/login"));

        step("Fill login form", () -> {
            $("#Email").setValue(login);
            $("#Password").setValue(password)
                    .pressEnter();
        });

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithApiTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookiesValue = given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    //.body("Email=" + login + "&Password=" + password + "&RememberMe=false")
                    //.body(format("Email=%s&Password=%s&RememberMe=false", login, password))
                    .log().all()
                    .when()
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract().cookie(authCookieName);


            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
            step("Set cookie to to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookiesValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });

        });

        step("Open main page", () ->
                open(""));
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithApiAndAllureListenerTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookiesValue = given()
                    .filter(new AllureRestAssured())
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


            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
            step("Set cookie to to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookiesValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });

        });

        step("Open main page", () ->
                open(""));
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithApiAndCustomListenerTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookiesValue = given()
                    .filter(withCustomTemplates())
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


            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
            step("Set cookie to to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookiesValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });

        });

        step("Open main page", () ->
                open(""));
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @DisplayName("")
    void addProductCartTest() {

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=5" +
                "7&addtocart_72.EnteredQuantity=1";

        String authCookiesValue = "C1B8AC121D6A98B39F388FF03D669B0900DA6E407EDAD226C2837025" +
                "F8B0FECE230F075863F9FAC5C5A9342EA44BD943ED1B8545336B5583FFD8F8FA6170AC643662EB26C86FED83F3E2C7F" +
                "802DEACEAB26788C5F90047F8E98F149CDD918405E8F8BB31EA887105C44F3CD33EE6CE80C8E9DCBA2B699DAEFDABC0B7" +
                "110A5293D325135D85DB33B7E8DD3848B444849F81D546687FC0612D6E086B0CCD2AE48D";

        step("Get cookie by api and set it to browser", () -> {
            String cartSize = given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    .body(body)
                    .cookie(authCookieName, authCookiesValue)
                    .log().all()
                    .when()
                    .post("/addproducttocart/details/72/1")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                    .body("updateflyoutcartsectionhtml", notNullValue())
                    .body("updatetopcartsectionhtml", notNullValue())
                    .extract()
                    .path("updatetopcartsectionhtml");

            {
                    /*
                    "success": true,
                    "message": "The product has been added to your <a href=\"/cart\">shopping cart</a>",
                    "updatetopcartsectionhtml": "(9)",
                    "updateflyoutcartsectionhtml":
                     */
            }
            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
            step("Set cookie to to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookiesValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });


            step("Open main page", () ->
                    open(""));
            step("Verify cart size", () ->
                    $("#topcartlink .cart-qty").shouldHave(text(cartSize)));

        });
    }

    @Test
    @DisplayName("")
    void addProductCartWithDynamicCookieTest() {

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

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=5" +
                "7&addtocart_72.EnteredQuantity=1";

        step("Get cookie by api and set it to browser", () -> {
            String cartSize = given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    .body(body)
                    .cookie(authCookieName, authCookiesValue)
                    .log().all()
                    .when()
                    .post("/addproducttocart/details/72/1")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                    .body("updateflyoutcartsectionhtml", notNullValue())
                    .body("updatetopcartsectionhtml", notNullValue())
                    .extract()
                    .path("updatetopcartsectionhtml");

            {
                    /*
                    "success": true,
                    "message": "The product has been added to your <a href=\"/cart\">shopping cart</a>",
                    "updatetopcartsectionhtml": "(9)",
                    "updateflyoutcartsectionhtml":
                     */
            }
            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
            step("Set cookie to to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookiesValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });


            step("Open main page", () ->
                    open(""));
            step("Verify cart size", () ->
                    $("#topcartlink .cart-qty").shouldHave(text(cartSize)));

        });
    }

    @Test
    @DisplayName("")
    void addProductCartWithNewUserTest() {

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=5" +
                "7&addtocart_72.EnteredQuantity=1";


        given()
                .filter(withCustomTemplates())
                .contentType("application/x-www-form-urlencoded")
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("updateflyoutcartsectionhtml", notNullValue())
                .body("updatetopcartsectionhtml", is("(1)"));
    }
}
