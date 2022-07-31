package ru.netology.domain;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.domain.entities.User;
import ru.netology.domain.utils.DataGenerator;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

public class AuthTest {
    // спецификация нужна для того, чтобы переиспользовать настройки в разных запросах
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999");
    }

    @Test
    void shouldAddUserAndAuthTest() {
        User user = DataGenerator.Registration.generateUser("ru", "active");
        SelenideElement form = $(".form");
        DataGenerator.printTestData(user);

        // запрос на добавление пользователся в тестовом режиме
        given() // "дано"
                // указываем, какую спецификацию используем
                .spec(requestSpec)
                // передаём в теле объект, который будет преобразован в JSON
                .body(user)
                // "когда"
                .when()
                // на какой путь, относительно BaseUri отправляем запрос
                .post("/api/system/users")
                // "тогда ожидаем"
                .then()
                // код 200 OKx
                .statusCode(200);

        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$(".button.button").click();

        $("body [id='root'] .App_appContainer__3jRx1").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    void shouldAddBlockedUserAndAuthTest() {
        User user = DataGenerator.Registration.generateUser("ru", "blocked");
        SelenideElement form = $(".form");
        DataGenerator.printTestData(user);

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$(".button.button").click();

        $(".notification .notification__content")
                .shouldHave(Condition.text("Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldAddNoValidateUserAndAuthTest() {
        User user = DataGenerator.Registration.generateUser("ru", "active");
        SelenideElement form = $(".form");
        String noValidateName = "monkey14";
        DataGenerator.printTestData(user);
        DataGenerator.printTestData(noValidateName, user.getPassword(), user.getStatus());

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        form.$("[data-test-id='login'] input").setValue(noValidateName);
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$(".button.button").click();

        $(".notification .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldAddUserNoValidatePasswordAndAuthTest() {
        User user = DataGenerator.Registration.generateUser("ru", "active");
        SelenideElement form = $(".form");
        String noValidatePassword = "monkey14@sdaf1234";
        DataGenerator.printTestData(user);
        DataGenerator.printTestData(user.getLogin(), noValidatePassword, user.getStatus());

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(noValidatePassword);
        form.$(".button.button").click();

        $(".notification .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }
}
