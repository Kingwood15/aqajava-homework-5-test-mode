package ru.netology.domain;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.domain.entities.User;
import ru.netology.domain.utils.DataGenerator;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {
    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999");
    }

    @Test
    void shouldAddUserAndAuthTest() {
        User user = DataGenerator.Registration.registrationUser("active");
        SelenideElement form = $(".form");

        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$(".button.button").click();

        $(".heading_theme_alfa-on-white").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    void shouldAddBlockedUserAndAuthTest() {
        User user = DataGenerator.Registration.registrationUser("blocked");
        SelenideElement form = $(".form");

        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$(".button.button").click();

        $(".notification .notification__content")
                .shouldHave(Condition.text("Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldAddNoValidateUserAndAuthTest() {
        User user = DataGenerator.Registration.registrationUser("active");
        SelenideElement form = $(".form");
        String noValidateName = DataGenerator.getRandomPassword();

        form.$("[data-test-id='login'] input").setValue(noValidateName);
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$(".button.button").click();

        $(".notification .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldAddUserNoValidatePasswordAndAuthTest() {
        User user = DataGenerator.Registration.registrationUser("active");
        SelenideElement form = $(".form");
        String noValidatePassword = DataGenerator.getRandomPassword();

        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(noValidatePassword);
        form.$(".button.button").click();

        $(".notification .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }
}
