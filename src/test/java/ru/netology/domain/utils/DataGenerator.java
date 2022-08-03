package ru.netology.domain.utils;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import ru.netology.domain.entities.User;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@UtilityClass
public class DataGenerator {
    // спецификация нужна для того, чтобы переиспользовать настройки в разных запросах
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private static void sendRequest(User user) {
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
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    @UtilityClass
    public static class Registration {
        public static User generateUser(String status) {
            return new User(getRandomLogin(), getRandomPassword(), status);
        }

        public static User registrationUser(String status) {
            User registeredUser = generateUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }
}
