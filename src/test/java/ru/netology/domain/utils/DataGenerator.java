package ru.netology.domain.utils;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;
import ru.netology.domain.entities.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@UtilityClass
public class DataGenerator {
    @UtilityClass
    public static class Registration {
        public static User generateUser(String locale, String status) {
            Faker faker = new Faker(new Locale(locale));
            return new User(faker.name().username(), faker.internet().password(), status);
        }
    }

    /**
     * Метод получения псевдослучайного целого числа от min до max (включая max);
     */
    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public void printTestData(String login, String password, String status) {
        System.out.println("\n" + login + "\n" + password + "\n" + status + "\n");
    }

    public void printTestData(User user) {
        printTestData(user.getLogin(), user.getPassword(), user.getStatus());
    }
}
